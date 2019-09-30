//package com.sziov;
//
//import org.apache.flume.Event;
//import org.omg.CORBA.Object;
//import org.omg.CORBA.*;
//import org.omg.PortableInterceptor.Interceptor;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MyInterceptor implements Interceptor {
//    // 原始数据字段的分隔符
//    private String fields_separator;
//    // 需要保留字段索引，形式如：1,2,3,4
//    private String indexs;
//    // indexs的分隔符
//    private String indexs_sparator;
//    // 需要加密字段索引
//    private String filed_encrypted_index;
//
//    public MyInterceptor(String fields_separator, String indexs, String indexs_sparator, String filed_encrypted_index) {
//        this.fields_separator = fields_separator;
//        this.indexs = indexs;
//        this.indexs_sparator = indexs_sparator;
//        this.filed_encrypted_index = filed_encrypted_index;
//    }
//
//    public void initialize() {
//    }
//
//    public Event intercept(Event event) {
//        if (event == null) return null;
//        // 通过获取event数据，转化成字符串
//        String line = new String(event.getBody(), Charsets.UTF_8);
//        // 通过分隔符分割数据
//        String[] split = line.split(fields_separator);
//        //获取需要保留的字段索引
//        String[] indexs_data = indexs.split(indexs_sparator);
//        // 需要加密的索引
//        int encrypted_index = Integer.parseInt(filed_encrypted_index);
//
//        String newLine = "";
//        for (int i = 0; i < indexs_data.length; i++) {
//            int index = Integer.parseInt(indexs_data[i]);
//            String filed = split[index];
//            if (index == encrypted_index) {// 字段需要加密处理
//                //通过MD5加密
//                filed = StringUtils.GetMD5Code(filed);
//            }
//            // 拼接字符串
//            newLine += filed;
//            //字段之间添加分隔符，舍弃最后一个
//            if (i < indexs_data.length - 1) {
//                newLine += fields_separator;
//            }
//        }
//        event.setBody(newLine.getBytes(Charsets.UTF_8));
//        return event;
//    }
//
//    public List<Event> intercept(List<Event> list) {
//        if (list == null) return null;
//        List<Event> events = new ArrayList<Event>();
//        for (Event event : list) {
//            Event intercept = intercept(event);
//            events.add(intercept);
//        }
//        return events;
//    }
//
//    public void close() {
//
//    }
//
//    public boolean _is_a(String repositoryIdentifier) {
//        return false;
//    }
//
//    public boolean _is_equivalent(Object other) {
//        return false;
//    }
//
//    public boolean _non_existent() {
//        return false;
//    }
//
//    public int _hash(int maximum) {
//        return 0;
//    }
//
//    public Object _duplicate() {
//        return null;
//    }
//
//    public void _release() {
//
//    }
//
//    public Object _get_interface_def() {
//        return null;
//    }
//
//    public Request _request(String operation) {
//        return null;
//    }
//
//    public Request _create_request(Context ctx, String operation, NVList arg_list, NamedValue result) {
//        return null;
//    }
//
//    public Request _create_request(Context ctx, String operation, NVList arg_list, NamedValue result, ExceptionList exclist, ContextList ctxlist) {
//        return null;
//    }
//
//    public Policy _get_policy(int policy_type) {
//        return null;
//    }
//
//    public DomainManager[] _get_domain_managers() {
//        return new DomainManager[0];
//    }
//
//    public Object _set_policy_override(Policy[] policies, SetOverrideType set_add) {
//        return null;
//    }
//
//    public String name() {
//        return null;
//    }
//
//    public void destroy() {
//
//    }
//
//    public static class Builder implements Interceptor.Builder {
//
//        // 原始数据字段的分隔符
//        private String fields_separator;
//        // 需要保留字段索引，形式如：1,2,3,4
//        private String indexs;
//        // indexs的分隔符
//        private String indexs_sparator;
//        // 需要加密字段索引
//        private String filed_encrypted_index;
//
//        public Interceptor build() {
//            return new MyInterceptor(fields_separator, indexs, indexs_sparator, filed_encrypted_index);
//        }
//
//        public void configure(Context context) {
//            fields_separator = context.getString(Constant.FIELDS_SEPARATOR, Constant.DEFAULT_FIELD_SEPARATOR);
//            indexs = context.getString(Constant.INDEXS, Constant.DEFAULT_INDEXS);
//            indexs_sparator = context.getString(Constant.INDEXS_SEPARATOR, Constant.DEFAULT_INDEXS_SEPARATOR);
//            filed_encrypted_index = context.getString(Constant.FIELD_ENCRYPTED_INDEX, Constant.DEFAULT_FIELD_ENCRYPTED_INDEX);
//        }
//    }
//}