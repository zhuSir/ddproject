package com.xmsmartcity.extra.weixin;

public enum TemplateEnum {
	
	//消息发送通知
	temp1{
		public String getId(){
			return "0ROMAAVEsjrePigk7ENDN3Ko3GcMF6gFY9WBzqqKg6s ";
		}
		public String getName(){
			return "\"data\":{\"first\": {\"value\":\"&firstData&\",\"color\":\"#173177\"},\"keyword1\": {\"value\":\"&keyword1Data&\",\"color\":\"#173177\"},\"keyword2\": {\"value\":\"&keyword2Data&\",\"color\":\"#173177\"},\"keyword3\": {\"value\":\"&keyword3Data&\",\"color\":\"#173177\"},\"remark\": {\"value\":\"&remarkData&\",\"color\":\"#173177\"}}";
		}
	},
	//流程待办提醒      有操作的事项
	
	//邀请成为负责人 ，申请加入团队，移交团队 ，邀请成为团队成员 ，移交甲方项目 ， 移交乙方负责人，提交工单
	temp2{
		public String getId(){
			return "4SwmMDbpiHr01ExyKU1vK71oI8NC1bT2VPn9Tu5vMJk";
		}
		public String getName(){
			return "\"data\":{\"first\": {\"value\":\"&firstData&\",\"color\":\"#173177\"},\"keyword1\": {\"value\":\"&keyword1Data&\",\"color\":\"#173177\"},\"keyword2\": {\"value\":\"&keyword2Data&\",\"color\":\"#173177\"},\"remark\": {\"value\":\"&remarkData&\",\"color\":\"#173177\"}}";
		}
	},
	
	//工单派发通知 
	//转派，增员
	temp3{
		public String getId(){
			return "audoow0KzpND8TF6ZdAP-0tKTgkXIiWnDRwx7Xhc2RY";
		}
		public String getName(){
			return "\"data\":{\"first\": {\"value\":\"#firstData#\",\"color\":\"#173177\"},\"keyword1\": {\"value\":\"#keyword1Data#\",\"color\":\"#173177\"},\"keyword2\": {\"value\":\"#keyword2Data#\",\"color\":\"#173177\"},\"remark\": {\"value\":\"#remarkData#\",\"color\":\"#173177\"}}";
		}
	},
	//申请加入通知
	//申请加入团队
	temp4{
		public String getId(){
			return "l8OZaMy6fE1TFNXVCQVmhDTVnY-E83bZDYeBqsrRm48";
		}
		public String getName(){
			return "\"data\":{\"first\": {\"value\":\"#firstData#\",\"color\":\"#173177\"},\"keyword1\": {\"value\":\"#keyword1Data#\",\"color\":\"#173177\"},\"keyword2\": {\"value\":\"#keyword2Data#\",\"color\":\"#173177\"},\"remark\": {\"value\":\"#remarkData#\",\"color\":\"#173177\"}}";
		}
	},
	
	//审核结果通知
		//信息提醒
		temp5{
			public String getId(){
				return "drV8SwFs_9enNCUKv4kgq1PwgjtJgiOUApZo6ig2kR8";
			}
			public String getName(){
				return "\"data\":{\"first\": {\"value\":\"&firstData&\",\"color\":\"#173177\"},\"keyword1\": {\"value\":\"&keyword1Data&\",\"color\":\"#173177\"},\"keyword2\": {\"value\":\"&keyword2Data&\",\"color\":\"#173177\"},\"remark\": {\"value\":\"&remarkData&\",\"color\":\"#173177\"}}";
			}
		},
		
		//退出通知
		//退出团队
		temp6{
			public String getId(){
				return "ZvFOTn7P5vVHnYMkMDmQHjj_H7IGFDH14J9Qg7wTrM4";
			}
			public String getName(){
				return "\"data\":{\"first\": {\"value\":\"#firstData#\",\"color\":\"#173177\"},\"keyword1\": {\"value\":\"#keyword1Data#\",\"color\":\"#173177\"},\"keyword2\": {\"value\":\"#keyword2Data#\",\"color\":\"#173177\"},\"remark\": {\"value\":\"#remarkData#\",\"color\":\"#173177\"}}";
			}
		};
	
	 public abstract String getName();
	 public abstract String getId();
}
