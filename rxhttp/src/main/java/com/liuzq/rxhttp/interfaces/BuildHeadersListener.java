package com.liuzq.rxhttp.interfaces;

import java.util.Map;

/**
 * desc    : 请求头interface
 */
public interface BuildHeadersListener {
    Map<String, String> buildHeaders();
}
