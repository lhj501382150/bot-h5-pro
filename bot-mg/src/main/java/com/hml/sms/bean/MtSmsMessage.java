package com.hml.sms.bean;

import java.util.List;
import java.util.Map;

public  class MtSmsMessage { 
        List<String> mobiles; 
 
 
        String templateId; 
 
 
        Map<String, String> templateParas; 
 
 
        String signature; 
 
 
        String messageId; 
 
 
        String extCode; 
 
 
        List<NamedPatameter> extendInfos; 
 
 
        /** 
         * 返回 mobiles 
         * 
         * @return mobiles值 
         */ 
        public List<String> getMobiles() { 
            return mobiles; 
        } 
 
 
        /** 
         * 对mobiles进行赋值 
         * 
         * @param mobiles mobiles值 
         */ 
        public void setMobiles(List<String> mobiles) { 
            this.mobiles = mobiles; 
        } 
 
 
        /** 
         * 返回 templateId 
         * 
         * @return templateId值 
         */ 
        public String getTemplateId() { 
            return templateId; 
        } 
 
 
        /** 
         * 对templateId进行赋值 
         * 
         * @param templateId templateId值 
         */ 
        public void setTemplateId(String templateId) { 
            this.templateId = templateId; 
        } 
 
 
        /** 
         * 返回 templateParas 
         * 
         * @return templateParas值 
         */ 
        public Map<String, String> getTemplateParas() { 
            return templateParas; 
        } 
 
 
        /** 
         * 对templateParas进行赋值 
         * 
         * @param templateParas templateParas值 
         */ 
        public void setTemplateParas(Map<String, String> templateParas) { 
            this.templateParas = templateParas; 
        } 
 
 
        /** 
         * 返回 signature 
         * 
         * @return signature值 
         */ 
        public String getSignature() { 
            return signature; 
        } 
 
 
        /** 
         * 对signature进行赋值 
         * 
         * @param signature signature值 
         */ 
        public void setSignature(String signature) { 
            this.signature = signature; 
        } 
 
 
        /** 
         * 返回 messageId 
         * 
         * @return messageId值 
         */ 
        public String getMessageId() { 
            return messageId; 
        } 
 
 
        /** 
         * 对messageId进行赋值 
         * 
         * @param messageId messageId值 
         */ 
        public void setMessageId(String messageId) { 
            this.messageId = messageId; 
        } 
 
 
        /** 
         * 返回 extCode 
         * 
         * @return extCode值 
         */ 
        public String getExtCode() { 
            return extCode; 
        } 
 
 
        /** 
         * 对extCode进行赋值 
         * 
         * @param extCode extCode值 
         */ 
        public void setExtCode(String extCode) { 
            this.extCode = extCode; 
        } 
 
 
        /** 
         * 返回 extendInfos 
         * 
         * @return extendInfos值 
         */ 
        public List<NamedPatameter> getExtendInfos() { 
            return extendInfos; 
        } 
 
 
        /** 
         * 对extendInfos进行赋值 
         * 
         * @param extendInfos extendInfos值 
         */ 
        public void setExtendInfos(List<NamedPatameter> extendInfos) { 
            this.extendInfos = extendInfos; 
        } 
 
 
    } 