package com.liuzq.searchbox.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.liuzq.commlibrary.recycler.RVHelper;
import com.liuzq.commlibrary.recycler.RecycleViewDivider;
import com.liuzq.commlibrary.utils.DataUtils;
import com.liuzq.commlibrary.utils.DisplayUtils;
import com.liuzq.flowlayout.FlowLayout;
import com.liuzq.flowlayout.TagAdapter;
import com.liuzq.flowlayout.TagFlowLayout;
import com.liuzq.flowlayout.TagView;
import com.liuzq.flowlayout.inter.OnTagClickListener;
import com.liuzq.searchbox.R;
import com.liuzq.searchbox.SearchHistoryAdapter;
import com.liuzq.searchbox.callback.OnSearchCallBackListener;
import com.liuzq.searchbox.data.BaseData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 搜索框 RecyclerView
 */
public class SearchLayout<T> extends LinearLayout implements View.OnClickListener,
        TextWatcher, TextView.OnEditorActionListener,
        BaseQuickAdapter.OnItemClickListener,
        OnTagClickListener {

    private Context context;
    private String msearch_hint; // 文本框Hint文字
    private int msearch_background; // 文本框Background
    private ImageView ib_searchtext_delete; // 搜索框删除
    private EditText et_searchtext_search; // 搜索框
    private Button bt_text_search_back; // 搜索or返回 按钮

    private LinearLayout searchview; // search view

    private TextView tvclearolddata; // 清除历史
    private LinearLayout ll_clear; // 清空历史搜索

    //热门搜索
    private TagFlowLayout hotflowLayout;
    //历史搜索
    private RecyclerView historyRecyclerView;
    private SearchHistoryAdapter<T> historyAdapter;

    // 历史搜索数据
    private List<BaseData<T>> historyDatas = new ArrayList<>();

    private String backtitle;//="取消";
    private String searchtitle;//="搜索";

    private int msearch_only_search;
    private static final int YES = 0;
    private static final int NO = 1;

    private int msearch_list_status;
    private static final int VISIBLE = 0;
    private static final int INVISIBLE = 1;
    private static final int GONE = -1;

    private int msearchSaveDataSize;//默认搜索记录的条数， 正确的是传入进来的条数
    private final int SAVE_DATA_SIZE = 6;

    private OnSearchCallBackListener<T> onSearchCallBackListener;

    public SearchLayout(Context context) {
        this(context, null);
    }

    public SearchLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.search_box);
        msearch_only_search = array.getInt(R.styleable.search_box_search_only_search, NO); // 是否只有搜索
        msearch_hint = array.getString(R.styleable.search_box_search_hint); // 搜索框Hint
        msearch_background = array.getResourceId(R.styleable.search_box_search_background, R.drawable.search_baground_shap); // 搜索背景图
        msearchSaveDataSize = array.getInteger(R.styleable.search_box_search_save_old_data_size, SAVE_DATA_SIZE); // 历史记录缓存数量
        array.recycle();
        initView();
    }

    private void initView() {
        backtitle= getResources().getString(R.string.search_cancel);
        searchtitle= getResources().getString(R.string.search_verify);
        searchview = (LinearLayout) View.inflate(context, R.layout.search_layout, null);
        //把获得的view加载到这个控件中
        addView(searchview);
        //把两个按钮从布局文件中找到
        ib_searchtext_delete = (ImageView) searchview.findViewById(R.id.ib_searchtext_delete);
        et_searchtext_search = (EditText) searchview.findViewById(R.id.et_searchtext_search);
        et_searchtext_search.setBackgroundResource(msearch_background);
        et_searchtext_search.setHint(msearch_hint);
        //搜索返回时一个按钮
        bt_text_search_back = (Button) searchview.findViewById(R.id.buttonback);
        if (msearch_only_search == YES) { //只有搜索
            bt_text_search_back.setText(searchtitle);
        } else if (msearch_only_search == NO) { //搜索or取消
            bt_text_search_back.setText(backtitle);
        }
        //清除历史记录
        tvclearolddata = (TextView) searchview.findViewById(R.id.tvclearolddata);
        //清空搜索记录
        ll_clear = (LinearLayout) findViewById(R.id.ll_clear);
        //热门搜索
        hotflowLayout =  (TagFlowLayout) searchview.findViewById(R.id.id_flowlayouthot);
        //历史搜索
        historyRecyclerView = searchview.findViewById(R.id.id_recyclerviewhistory);

        setListener();
    }

    private void setListener() {
        //给删除按钮添加点击事件 搜索框删除
        ib_searchtext_delete.setOnClickListener(this);
        //搜索返回时一个按钮
        bt_text_search_back.setOnClickListener(this);
        //清除历史
        tvclearolddata.setOnClickListener(this);
        //清空历史搜索
        ll_clear.setOnClickListener(this);

        //给编辑框添加文本改变事件
        et_searchtext_search.addTextChangedListener(this);
        //软键盘搜索 KeyEvent
        et_searchtext_search.setOnEditorActionListener(this);
        // 热门搜索
        hotflowLayout.setOnTagClickListener(this);
    }

    /**
     * 设置接口回调
     * @param onSearchCallBackListener   setCallBackListener接口
     */
    private void setCallBackListener(OnSearchCallBackListener<T> onSearchCallBackListener){
        this.onSearchCallBackListener = onSearchCallBackListener;
    }

    public void initData(@Nullable List<BaseData<T>> olddata,
                         @Nullable List<BaseData<T>> hotdata,
                         OnSearchCallBackListener<T> sCb,
                         final int styleId) {
        setCallBackListener(sCb);
        hotflowLayout.removeAllViews();
        historyDatas.clear();
        if (DataUtils.isNotEmpty(olddata)) {
            historyDatas.addAll(olddata);
        }
        ll_clear.setVisibility(VISIBLE);

        final LayoutInflater mInflater = LayoutInflater.from(context);
        // 热门搜索
        hotflowLayout.setAdapter(new TagAdapter<BaseData<T>>(hotdata) {
            @Override
            public View getView(FlowLayout parent, int position, BaseData<T> tBaseData) {
                TextView tv = (TextView) mInflater.inflate(R.layout.adapter_hot_item, hotflowLayout, false);
                tv.setText(tBaseData.getSearch());
                tv.getBackground().setLevel(MyRandom(1, styleId));
                return tv;
            }
        });
        
        // 历史搜索
        RVHelper.initRecyclerViewStyle(context, historyRecyclerView, LinearLayout.VERTICAL);
        historyAdapter = new SearchHistoryAdapter<>();
        historyRecyclerView.setAdapter(historyAdapter);
        historyAdapter.setOnItemClickListener(this);
        historyRecyclerView.addItemDecoration(
                new RecycleViewDivider(context, LinearLayout.HORIZONTAL,
                DisplayUtils.dip2px(context, 1), 
                DataUtils.resColor(context, R.color.white_f5)));
        historyAdapter.setNewData(historyDatas);
    }

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        String string = ((TextView)((TagView)view).getTagView()).getText().toString();
        executeSearch_and_NotifyDataSetChanged(string);
        return true;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if(onSearchCallBackListener != null){
            onSearchCallBackListener.search(historyAdapter.getItem(position).getSearch().trim());
            et_searchtext_search.setText(historyAdapter.getItem(position).getSearch().trim());
            et_searchtext_search.setCursorVisible(false);
            et_searchtext_search.setSelection(et_searchtext_search.getText().toString().length());
            //点击项移动到首部位
            swap(historyDatas, position,0);
            historyAdapter.notifyDataSetChanged();
        }
    }
    
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ib_searchtext_delete) { // 搜索框删除
            // TODO Auto-generated method stub
            et_searchtext_search.setText("");
        } else if (id == R.id.buttonback) { // 搜索or返回 按钮
            String searchtext = et_searchtext_search.getText().toString().trim();

            if (DataUtils.isEquals(bt_text_search_back.getText().toString(), searchtitle)) { // 比较
                executeSearch_and_NotifyDataSetChanged(searchtext);
            } else {
                if (onSearchCallBackListener != null)
                    onSearchCallBackListener.back();
            }
        } else if (id == R.id.tvclearolddata) { // 清除历史
            if(onSearchCallBackListener != null) {
                historyDatas.clear();
                historyAdapter.notifyDataSetChanged();
                onSearchCallBackListener.clearOldData();
            }
        }

        else if (id == R.id.ll_clear) { // 清空历史搜索
            if(onSearchCallBackListener != null) {
                historyDatas.clear();
                historyAdapter.notifyDataSetChanged();
                onSearchCallBackListener.clearOldData();
            }
            ll_clear.setVisibility(View.GONE);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        et_searchtext_search.setCursorVisible(true);
        et_searchtext_search.setSelection(et_searchtext_search.getText().toString().length());
    }

    //当文本改变时候的操作
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (msearch_only_search == YES) {
            //如果编辑框中文本的长度大于0就显示删除按钮否则不显示
            if (s.length() > 0) {
                ib_searchtext_delete.setVisibility(View.VISIBLE);
            } else {
                ib_searchtext_delete.setVisibility(View.GONE);
            }
        } else if (msearch_only_search == NO) {
            //如果编辑框中文本的长度大于0就显示删除按钮否则不显示
            if (s.length() > 0) {
                ib_searchtext_delete.setVisibility(View.VISIBLE);
                bt_text_search_back.setText(searchtitle);
            } else {
                ib_searchtext_delete.setVisibility(View.GONE);
                bt_text_search_back.setText(backtitle);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH
                || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

            String searchtext = et_searchtext_search.getText().toString().trim();
            //do some thing
            executeSearch_and_NotifyDataSetChanged(searchtext);
            return true;
        }
        return false;
    }

    /**
     * 数据处理
     *
     * @param str
     */
    private void executeSearch_and_NotifyDataSetChanged(String str){
        if(onSearchCallBackListener != null && DataUtils.isNotEmpty(str)){
            if (historyDatas.size() > 0 && DataUtils.isEquals(historyDatas.get(0).getSearch(), str)) {
                ll_clear.setVisibility(VISIBLE);
            } else {
                if (historyDatas.size() == msearchSaveDataSize && historyDatas.size() > 0)
                    historyDatas.remove(historyDatas.size() - 1);
                BaseData data = new BaseData();
                data.setSearch(str);
                //判断字符串是否在历史记录中 如果在就不添加，反之则添加
                if (historyDatas != null && !historyDatas.contains(str))
                {
                    historyDatas.add(0, data);//把最新的添加到前面
                    historyAdapter.notifyDataSetChanged();
                    onSearchCallBackListener.saveOldData(historyDatas);
                }else {
                    historyDatas.remove(str);
                    historyDatas.add(0, data);//把最新的添加到前面
                    historyAdapter.notifyDataSetChanged();
                    onSearchCallBackListener.saveOldData(historyDatas);
                }
            }
            ll_clear.setVisibility(VISIBLE);
            et_searchtext_search.setText(str);
            onSearchCallBackListener.search(str);
            et_searchtext_search.setCursorVisible(false);
            et_searchtext_search.setSelection(et_searchtext_search.getText().toString().length());
        }
    }
    
    /**
     * 生成随机数
     * @param max  最大值
     * @param min   最小值
     * @return
     */
    public int MyRandom(int min,int max){
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }

    /**
     * 调换集合中两个指定位置的元素, 若两个元素位置中间还有其他元素，需要实现中间元素的前移或后移的操作。
     * @param list 集合
     * @param oldPosition 需要调换的元素
     * @param newPosition 被调换的元素
     * @param <T>
     */
    public static <T> void swap(List<BaseData<T>> list, int oldPosition, int newPosition){
        if(null == list){
            throw new IllegalStateException("集合不能为空");
        }
        BaseData<T> tempElement = list.get(oldPosition);

        // 向前移动，前面的元素需要向后移动
        if(oldPosition < newPosition){
            for(int i = oldPosition; i < newPosition; i++){
                list.set(i, list.get(i + 1));
            }
            list.set(newPosition, tempElement);
        }
        // 向后移动，后面的元素需要向前移动
        if(oldPosition > newPosition){
            for(int i = oldPosition; i > newPosition; i--){
                list.set(i, list.get(i - 1));
            }
            list.set(newPosition, tempElement);
        }
    }
}
