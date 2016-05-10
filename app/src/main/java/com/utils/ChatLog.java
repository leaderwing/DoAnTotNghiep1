package com.utils;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 5/10/2016.
 */
public class ChatLog extends ParseObject {
public List<String> getContents()
{
    return getList("contents");
}
    public void setContents(List<String> list)
    {
        put("contents" ,list);
    }
}
