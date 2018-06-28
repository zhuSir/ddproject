package com.xmsmartcity.util;

import java.util.HashMap;

public class Constant {

	//private static Logger logger = LoggerFactory.getLogger(Constant.class);
	
	public static PropertiesHelper helper = new PropertiesHelper("system.properties");
	// 新浪的根据IP地址返回对应的省市URL
	public static String IP_CONVERT_URL = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=xml&ip=";
	
	public static boolean Debug = helper.getBoolean("debug");
//	public static final String Content_Path= Debug ? "182.92.235.189:8077":"182.92.235.189:8077";

	public static String apikey = "5ceccf1ee5f43d21d7b437731f95ecd5";
	public static String apikey_marketing = "a30c4c4c4cade805e126d57ae4ad10b0";
	
	public static String MAIL="service@dweibao.com";
	
//	//客服邀请团队负责人
//	public static long admin_invitation_company_tpl_id = 1675074;
	//客服邀请乙方负责人
//	public static long admin_invitation_project_b_tpl_id = 1675082;
	//客服邀请项目负责人
//	public static long admin_invitation_project_tpl_id = 1675086;
	
	// 设置模板ID 模板:【咚咚维护平台】您的验证码是#code#
	public static long tpl_id = 1675010;
	
	//消息是否读取
	public static final Integer READED = 1;
	public static final Integer UNREAD = 0;
	
	public static final String webSite = "www.dweibao.com";
	
	public static final String hostSite = Debug ? "maintainst.dweibao.com" : "maintain.dweibao.com";//"10.100.1.9:8080";//"maintain.dweibao.com";
	
	public static final String SUCCESS = "success";
	public static final String INFO = "info";
	public static final String UTF8 = "UTF-8";
	public static final String TEXT_HTML = "text/html";
	public static final String EMPRTY = "";
	public static final String EMPRTY_DATE = "0000-00-00";
	//万能验证码
	public static String WANNENG_CODE = "LADYGAGA714";//iiezlse621
	//版本号
	public static String A_VERSION = "1.0.0";
	public static String B_VERSION = "1.0.0";
	//验证码有效时间5分钟
	public static final long CODE_VALIDATE_TIME =5*60*1000; 
	public static final int ZERO = 0;
	public static final int ONE = 1;
	public static final int TWO = 2;
	public static final int THREE = 3;
	public static final int FOUR = 4;
	public static final int FIVE = 5;
	public static final int SIX = 6;
	public static final int SEVEN = 7;
	public static final int EIGHT = 8;	
	public static final int NINE = 9;
	public static final int TEN = 10;
	public static final int ELEVEN = 11;
	public static final int TWELVE = 12;
	public static final int THIRTEEN = 13;
	public static final int FOURTEEN = 14;
	public static final int FIFTEEN = 15;
	public static final int SIXTEEN = 16;
	public static final int SEVENTEEN = 17;
	
	public static final String DEFAULT_PASSWORD="12345678";
	public static final String SERVICE_ERROR = "服务器异常，请稍后重试";
	public static final String PARAMS_ERROR = "paramsError_参数错误，请联系开发人员";
	public static final String EMPTY_TOKEN = "token不能为空，请联系开发人员";
	public static final String ERROR_TOKEN = "token错误，请重新登录";
	public static final String EMPTY_CODE = "验证码不能为空";
	public static final String EMPTY_PHONE = "手机号不能为空";
	public static final String EMPTY_NAME = "用户名不能为空";
	public static final String EMPTY_PASSWORD = "密码不能为空";
	public static final String EMPTY_SEARCH = "搜索内容不能为空";
	public static final String EMPTY_INTRODUCE = "报障内容不能为空";
	public static final String FAULT_INFO_ERROR = "当前报障单已被关闭";
	public static final String FAULT_INFO_STATE_ERROR = "当前状态不对，请刷新重试";
	public static final String EMPTY_EVALUATE_STAR = "请选择评价的星级";
	public static final String TOO_MANY_WORD = "简介字数过多";
	public static final String EVALUATE_TOO_MANY_WORD = "评价字数过多";
	public static final String ALREADY_SIGN = "您今天已经签到过了";
	public static final String EMPTY_REASON = "理由不能为空";
	public static final String CODE_ERROR = "输入的验证码有误，请重新输入";
	public static final String USER_EXIST = "该手机号已经注册过了";
	public static final String USER_NO_EXIST = "该手机号尚未注册";
	public static final String NO_PROJECT_POWER = "您不是该项目负责人，暂无权限删除他人";
	public static final String NO_COMPANY_POWER = "您不是该团队负责人，暂无权限删除他人";
	public static final String NO_FINISH_FAULTINFO = "该成员当前有未完成的报障单，暂时无法删除";
	public static final String HAVE_NO_FINISH_FAULTINFO = "您当前有未完成的报障单，暂时无法退出";
	public static final String ADVICE_CHANGE_LEADER = "该成员是项目负责人，请先转让负责人";
	public static final String NOT_CHANGE_LEADER = "您当前是项目负责人，请先转让负责人";
	public static final String ALREADY_HAS_COMPANY_ME = "您已经有所属团队了哦，当前操作无效";
	public static final String ALREADY_HAS_COMPANY_OTHER = "对方已经有所属团队了哦，当前操作无效";
	public static final String MESSAGE_STATE_ERROR = "消息状态错误，请刷新重试";
	public static final String PASSWORD_ERROR = "密码错误";
	public static final String PASSWORD_NULL = "密码不能为空";
//	public static final String CODE_215217 = "215217";
	public static final String USER_INFO_ERROR = "手机号或密码错误";
	public static final String DEFAULT_HEAD_PIC = "user_head_pic.png"; //默认用户头像
	public static final String DEFAULT_COMPANYER_HEAD_PIC = "companyer_head_pic.png"; //默认团队负责人头像
	public static final String DEFAULT_PROJECTER_HEAD_PIC = "projecter_head_pic.png"; //默认项目负责人头像
	public static final String DEFAULT_WORKER_HEAD_PIC = "worker_head_pic.png"; //默认维保人员头像
	public static final String DEFAULT_SERVER_PIC = "service_default_pic.png"; //客服图片
	public static final String DEFAULT_COMPANY_PIC = "default_company_pic.png";//默认团队图片
	public static final String DEFAULT_PROJECT_PIC = "default_project_pic.png";//默认项目图片
	public static final String DEFAULT_SERVER_NAME = "咚咚客服";
	public static final String EMPRTY_REFUSE_REASON = "拒绝理由不能为空";
	public static final String NO_PROJECT_RESPONSER = "您已非当前项目负责人，请刷新重试";
	public static final String NO_PROJECT_USER = "您已非当前项目成员，请刷新重试";
	public static final String HAS_INVITED = "该用户已经邀请过了，请勿重复邀请";
	public static final String HAVE_COMPANY_PROJECT = "您还有未移交的团队项目，无法退出";
	public static final String HAVE_COMPANY_PROJECT_TO_DEL_COMPANY_USER = "选择的成员存在未移交的团队项目，无法删除";
	public static final String LOGIN_TIMEOUT = "登录过期，请重新登录";
	public static final String NOT_JOIN_COMPANY = "您还未加入团队，快去加入团队吧";
	public static final String HAS_PROJECT_INVITE = "该用户已发送邀请成为乙方负责人的消息，暂未处理";
	public static final String BE_BAN = "您的账号已被禁用，请联系客服人员";
	
	//巡检Quartz相关
	public static final String PATROLSCHEME_JOB_GROUP = "PATROLSCHEME_JOBGROUP";//巡检任务JOB组名称
	public static final String PATROLSCHEME_TIRIGGER_GROUP = "PATROLSCHEME_TIRIGGER_GROUP";//巡检任务TIRIGGER组名称
	public static final String PATROLSCHEME_REMIND_JOB_KEY = "1";//提醒巡检JOBKEY
	public static final String PATROLSCHEME_REMIND_JOB_GROUP = "PATROLSCHEME_REMIND_JOBGROUP";//提醒巡检名称
	public static final String PATROLSCHEME_REMIND_TIRIGGER_GROUP = "PATROLSCHEME_REMIND_TIRIGGER_GROUP";//提醒巡检TIRIGGER组
	public static final String PATROLSCHEME_MISSING_JOB_KEY = "1";//漏巡检JOBKEY
	public static final String PATROLSCHEME_MISSING_JOB_GROUP = "PATROLSCHEME_MISSING_JOBGROUP";//漏巡检名称
	public static final String PATROLSCHEME_MISSING_TIRIGGER_GROUP = "PATROLSCHEME_MISSING_TIRIGGER_GROUP";//漏巡检TIRIGGER组

	public interface PHONE_MESSAGE_INDEX{
		public static Integer user_invitation_project_a_tpl_id_index = 1;		//用户邀请项目甲方负责人     
		public static Integer user_invitation_project_b_tpl_id_index =2;		//用户邀请乙方负责人
		public static Integer user_invitation_company_tpl_id_index = 3;		//用户邀请团队成员
		public static Integer user_invitation_project_a_member_tpl_id_index = 4;		//用户邀请项目甲方成员
		public static Integer user_invitaion_project_b_member_tpl_id_index = 5;		//用户邀请项目乙方成员
		public static Integer user_invitation_company_principal_tpl_id_index = 6;		//邀请成为团队负责人
		public static Integer user_invitation_project_principal_tpl_id_index = 7;		//邀请成为项目负责人//暂时无用
		public static Integer user_reported_fault_tpl_id_index = 8;		//报障相关
		public static Integer user_timeout_fault_torevice_tpl_id_index = 9;		//维保超时提醒
		public static Integer user_timeout_maintain_torevice_tpl_id_index = 10;		//维保超时提醒
		public static Integer user_timeout_fault_process_tpl_id_index = 11;		//维保超时提醒
		public static Integer user_timeout_fault_havesend_tpl_id_index = 12;		//维保超时提醒
		public static Integer user_reported_fault_receive_tpl_id_index = 13;		//乙方接受报障单
		public static Integer user_reported_fault_repairing_tpl_id_index = 14;		//乙方正在维修
		public static Integer user_reported_fault_repaired_tpl_id_index = 15;		//乙方完成维修
		public static Integer user_reported_fault_operate_tpl_id_index = 16;		//乙方受理报障单
		
		public static Integer user_submit_fault_tpl_id_index = 17;                //提交报障单
		public static Integer user_submit_work_order_tpl_id_index = 18;		 	  //提交工单费用
		
		public static Integer user_return_fault_tpl_id_index=19;                  //维修工程师退回报修单
		
		//提前一天巡检提醒   referId存的是patrol_scheme表ID
		public static Integer user_patrol_remind_tpl_id_index = 100;  
		//漏巡检提醒  referId存的是patrol_scheme_record表ID
		public static Integer user_patrol_missing_tpl_id_index = 101;                  
	}
	
	//用户邀请项目甲方负责人     
	//【咚咚维护平台】#username#邀请您成为#project#项目负责人，赶快去注册确认吧！网址：#remark#回T退订
	public static long user_invitation_project_a_tpl_id = 1675072;
	//用户邀请乙方负责人
	//【咚咚维护平台】#username#邀您成为#project#项目乙方#servicetype#负责人，赶快去注册确认吧！网址：#remark#回T退订
	public static long user_invitation_project_b_tpl_id = 1675080;
	//用户邀请公司成员
	//【咚咚维护平台】#username#邀您成为#company#公司成员，赶快去注册确认吧！网址：#remark#回T退订
	@Deprecated//暂时无用
	public static long user_invitation_company_tpl_id = 1675076;
	//用户邀请项目甲方成员
	//【咚咚维护平台】#username#邀您成为#project#项目甲方成员，赶快去注册确认吧！网址：#remark#回T退订
	@Deprecated//暂时无用
	public static long user_invitation_project_a_member_tpl_id = 1764560;
	//用户邀请项目乙方成员
	//【咚咚维护平台】#username#邀您成为#project#项目乙方#servicetype#成员，赶快去注册确认吧！网址：#remark#回T退订
	@Deprecated//暂时无用
	public static long user_invitaion_project_b_member_tpl_id = 1764558;		
	//邀请成为公司负责人
	//【咚咚维护平台】#username#邀请您成为#company#公司负责人，赶快去注册确认吧！网址：#remark#回T退订回T退订
	public static long user_invitation_company_principal_tpl_id= 1764508;
	//邀请成为项目负责人
	//【咚咚维护平台】#username#邀请您成为#project#项目负责人，赶快去注册确认吧！网址：#remark#回T退订
	public static long user_invitation_project_principal_tpl_id = 1675072;
	//项目报障相关
	//【咚咚维护平台】#project#项目新增报障单，快去查看吧~ 网址：#remark#回T退订
	public static long user_reported_fault_tpl_id = 1772678;
	//报障相关
	//【咚咚维护平台】您有一个#faultname#报障单（报账单号:#code#）正在执行中，快去看看吧~网址：#remark#回T退订
	public static long user_reported_fault_info_tpl_id = 1802236;
	//维保超时提醒
	//【咚咚维护平台】报障单编号：#faultInfoCode#的维保方长时间未接受报障，请联系维保方人员。网址：#remark#回T退订
	public static long user_timeout_fault_torevice_tpl_id = 1803480;	
	//维保超时提醒
	//【咚咚维护平台】您的报障单编号：#faultInfoCode#还未接收报障，快去看看吧！网址：#remark#回T退订
	public static long user_timeout_maintain_torevice_tpl_id = 1803494;	
	//维保超时提醒
	//【咚咚维护平台】报障单编号：#faultInfoCode#的工程师请求增援或转派已超过1小时，快去看看吧！网址：#remark#回T退订
	public static long user_timeout_fault_process_tpl_id = 1803498;
	//维保超时提醒
	//【咚咚维护平台】报障单编号：#faultInfoCode#已转派给您超过一小时，快去看看吧！网址：#remark#回T退订
	public static long user_timeout_fault_havesend_tpl_id = 1803506;
	//【咚咚维护平台】#username#接收了您的报障单,等待处理中，详情请登录咚咚维保报修助手查看。回T退订
	public static long user_reported_fault_receive_tpl_id_index = 1889498;
	//【咚咚维护平台】维保人员#username#正在维修您提交的报障单，详情请登录咚咚维保报修助手查看。回T退订
	public static long user_reported_fault_repairing_tpl_id_index = 1889538;
	//【咚咚维护平台】您提交的报障单，维保人员：#username#完成了维修，详情请登录咚咚维保报修助手查看。回T退订
	public static long user_reported_fault_repaired_tpl_id_index = 1889546;
	//【咚咚维护平台】#username#受理了您的报障单,预约处理时间：#time#，详情请登录咚咚维保报修助手查看。回T退订
	public static long user_reported_fault_operate_tpl_id_index = 1889530;
	
	//邀请项目成员
	//【咚咚维护平台】#username#（#phone#）邀请您加入“#projectname#”维保项目，点击#remork#马上加入。回T退订
	public static long user_invite_project_member_tpl_id_index = 1898932;
	
	//用户邀请公司成员，短信中网址可绑定公司
	//【咚咚维护平台】#username#（#phone#）邀请您加入“#companyname#”公司，点击#remork#马上加入。回T退订
	public static long user_invite_company_member_tpl_id_index = 1898980;
	
	//用户提交了工单
	//【咚咚维护平台】#username#你好，#referUsername#提交了“#content#”故障，请尽快处理。回T退订
	public static long user_submit_fault_tpl_id = 1946130;
	
	//用户提交了费用工单
	//【咚咚维护平台】#username#你好，#referUsername#已经提交关于“#content#”故障的维修费用，请尽快确认。回T退订
	public static long user_submit_work_order_tpl_id = 1946136;
	
	//维修工程师退回报修单
	//【咚咚维护平台】#username#维修工程师退回了#projectname#项目，快进入微信小程序：”咚咚维保报修助手“查看吧～。回T退订
	public static long user_return_fault_tpl_id=2092366;
	
	public static HashMap<Integer , Long> PHONE_MESSAGE_MAP(){
		HashMap<Integer , Long> MAP = new HashMap<Integer, Long>();
		MAP.put(PHONE_MESSAGE_INDEX.user_invitation_project_a_tpl_id_index, user_invitation_project_a_tpl_id);
		MAP.put(PHONE_MESSAGE_INDEX.user_invitation_project_b_tpl_id_index, user_invitation_project_b_tpl_id);
		MAP.put(PHONE_MESSAGE_INDEX.user_invitation_company_tpl_id_index, user_invite_company_member_tpl_id_index);
		MAP.put(PHONE_MESSAGE_INDEX.user_invitation_project_a_member_tpl_id_index, user_invite_project_member_tpl_id_index);
		MAP.put(PHONE_MESSAGE_INDEX.user_invitaion_project_b_member_tpl_id_index, user_invite_project_member_tpl_id_index);
		MAP.put(PHONE_MESSAGE_INDEX.user_invitation_company_principal_tpl_id_index, user_invitation_company_principal_tpl_id);
		MAP.put(PHONE_MESSAGE_INDEX.user_invitation_project_principal_tpl_id_index, user_invitation_project_principal_tpl_id);
		MAP.put(PHONE_MESSAGE_INDEX.user_reported_fault_tpl_id_index, user_reported_fault_info_tpl_id);
		MAP.put(PHONE_MESSAGE_INDEX.user_timeout_fault_torevice_tpl_id_index, user_timeout_fault_torevice_tpl_id);
		MAP.put(PHONE_MESSAGE_INDEX.user_timeout_maintain_torevice_tpl_id_index, user_timeout_maintain_torevice_tpl_id);
		MAP.put(PHONE_MESSAGE_INDEX.user_timeout_fault_process_tpl_id_index, user_timeout_fault_process_tpl_id);
		MAP.put(PHONE_MESSAGE_INDEX.user_timeout_fault_havesend_tpl_id_index, user_timeout_fault_havesend_tpl_id);
		MAP.put(PHONE_MESSAGE_INDEX.user_reported_fault_receive_tpl_id_index, user_reported_fault_receive_tpl_id_index);
		MAP.put(PHONE_MESSAGE_INDEX.user_reported_fault_repairing_tpl_id_index, user_reported_fault_repairing_tpl_id_index);
		MAP.put(PHONE_MESSAGE_INDEX.user_reported_fault_repaired_tpl_id_index, user_reported_fault_repaired_tpl_id_index);
		MAP.put(PHONE_MESSAGE_INDEX.user_reported_fault_operate_tpl_id_index, user_reported_fault_operate_tpl_id_index);
		MAP.put(PHONE_MESSAGE_INDEX.user_submit_fault_tpl_id_index, user_submit_fault_tpl_id);
		MAP.put(PHONE_MESSAGE_INDEX.user_submit_work_order_tpl_id_index, user_submit_work_order_tpl_id);
		MAP.put(PHONE_MESSAGE_INDEX.user_return_fault_tpl_id_index, user_return_fault_tpl_id);
		return MAP;
	}
	
	public interface APP_TYPE{
		public static Integer A = 0;//甲方
		public static Integer B = 1;//乙方
	}
	//性别
	public interface SEX{
		public static int UNKNOW= 0;//保密
		public static int MAN= 1;//男
		public static int WOMAN= 2;//女
	}
	public interface SEX_STR{
		public static String UNKNOW_STR= "保密";
		public static String MAN_STR= "男";
		public static String WOMAN_STR= "女";
	}
	public static HashMap<Integer, String> SEX_MAP(){
		HashMap<Integer, String> MAP = new HashMap<Integer, String>();
		MAP.put(SEX.UNKNOW,SEX_STR.UNKNOW_STR);
		MAP.put(SEX.MAN,SEX_STR.MAN_STR);
		MAP.put(SEX.WOMAN,SEX_STR.WOMAN_STR);
		return MAP;
	}
	
	//用户角色
	public interface USER_ROLE_INDEX{
		public static int GENERAL_USER= 0;//普通用户
		public static int COMPANY_USER= 1;//团队负责人
		public static int PROJECT_USER= 2;//项目负责人
		public static int WORKER_USER= 3;//维保人员
	}
	public interface USER_ROLE_STR{
		public static String GENERAL_USER= "普通用户";
		public static String COMPANY_USER= "团队负责人";
		public static String  PROJECT_USER= "项目负责人";
		public static String WORKER_USER= "维保人员";
	}
	public static HashMap<Integer, String> USER_ROLE_MAP(){
		HashMap<Integer, String> MAP = new HashMap<Integer, String>();
		MAP.put(USER_ROLE_INDEX.GENERAL_USER,USER_ROLE_STR.GENERAL_USER);
		MAP.put(USER_ROLE_INDEX.COMPANY_USER,USER_ROLE_STR.COMPANY_USER);
		MAP.put(USER_ROLE_INDEX.PROJECT_USER,USER_ROLE_STR.PROJECT_USER);
		MAP.put(USER_ROLE_INDEX.WORKER_USER,USER_ROLE_STR.WORKER_USER);
		return MAP;
	}
	
	public static HashMap<Integer,String> USER_HEAD_PIC_MAP(){
		HashMap<Integer, String> MAP = new HashMap<Integer, String>();
		MAP.put(USER_ROLE_INDEX.GENERAL_USER,DEFAULT_HEAD_PIC);
		MAP.put(USER_ROLE_INDEX.COMPANY_USER,DEFAULT_COMPANYER_HEAD_PIC);
		MAP.put(USER_ROLE_INDEX.PROJECT_USER,DEFAULT_PROJECTER_HEAD_PIC);
		MAP.put(USER_ROLE_INDEX.WORKER_USER,DEFAULT_WORKER_HEAD_PIC);
		
		return MAP;
	}
	
	/*
	 * 报障类型 
	 * 			0;//直接报障
	 * 			1;//第三方报障
	 */
	public interface FAULT_INFO_TYPE{
		public static int DIRECTLY_REPORTED = 0;//直接报障
		public static int OTHER_REPORTED = 1;//第三方报障
	}
	
	/*
	 * 0全部 1待接收 2已退回 3待处理 4已接收 5待确认 6维修中 7待验收 8待评价 9已完成
	 */
	public interface FAULT_INFO_STATE{
		public static int ALL = 0;//0 全部
		public static int WAIT_RECEIVE = 1; //待接收
		public static int RETURNED = 2; //已退回
		public static int WAIT_CONDUCT = 3; //待处理
		public static int RECEIVED = 4;//已接收
		public static int WAIT_CONFIRM = 5;//待确认
		public static int MAINTAINING = 6; //维修中
		public static int WAIT_ACCEPTANCE = 7; //待验收
		public static int WAIT_EVALUATE = 8; //待评价
		public static int FINISH = 9; //已完成
		public static int CLOSE = 10; //已关闭
//		public static int REFUSE = 10; //乙方拒绝接受报障，待甲方确认
//		public static int REVOKE = 11; //乙方撤销接受报障（未填写工单），待甲方确认
//		public static int REVOKE_ORDER_REFUSE = 12; //乙方撤销接受报障（拒绝工单），待甲方确认
//		public static int REVOKE_WAIT_CONFIRM  = 13; //乙方撤销接受报障(工单待确定)，待甲方确认
	}
	
	public interface FAULT_INFO_STATE_STR{
		public static String ALL = "全部";
		public static String WAIT_RECEIVE =  "待接收";
		public static String RETURNED = "已退回";
		public static String WAIT_CONDUCT = "待处理"; 
		public static String RECEIVED = "已接收";
		public static String WAIT_CONFIRM = "待确认";
		public static String MAINTAINING= "维修中";
		public static String WAIT_ACCEPTANCE = "待验收";
		public static String WAIT_EVALUATE = "待评价";
		public static String FINISH = "已完成";
		public static String CLOSE = "已关闭";
//		public static String REFUSE = "已拒绝";
//		public static String REVOKE = "已撤销";
//		public static String REVOKE_ORDER_REFUSE = "已撤销";
//		public static String REVOKE_WAIT_CONFIRM  = "已撤销";
	}
	
	
	/*
	 * 报障状态
	 * 		待接收	0:乙方负责人接收分派报障单 1:乙方负责人接收报障单  2:乙方负责人退回报障单 
	 * 		待处理	3:乙方负责人转派报障单  4:乙方处理报障单  5:乙方负责人拒绝转派  6:乙方负责人增援  7:乙方负责人拒绝增援
	 *      已退回	8:甲方同意乙方退回  9:甲方重新发起报障单  
	 *      已接收	10:乙方请求转派  11:填写工单 12:无需工单
	 *      待确认	13:工单不通过 14:工单通过 
	 *      维修中	15:请求增援 16:维修说明 17:完成维修
	 *      待验收	18:验收合格 19:验收不合格  
	 *      待评价	20:工单评价
	 */
	/*
	 * 0:乙方负责人接收分派报障单
	 */
	public static Integer FAULT_RECEIVE_ASSIGN = 0;
	/*
	 * 1:乙方负责人接收报障单
	 */
	public static Integer FAULT_RECEIVE = 1;
	/*
	 *  2:乙方负责人退回报障单 
	 */
	public static Integer FAULT_RETURNED =2;
	/*
	 * 3:乙方负责人转派报障单
	 */
	public static Integer FAULT_REDEPLOY = 3;
	/*
	 * 4:乙方处理报障单
	 */
	public static Integer FAULT_OPERATE=4;
	/*
	 * 5:乙方负责人拒绝转派
	 */
	public static Integer FAULT_REFUSE_REDEPLOY=5;
	/*
	 * 6:乙方负责人增援
	 */
	public static Integer FAULT_REINFORCE=6;
	/*
	 * 7:乙方负责人拒绝增援
	 */
	public static Integer FAULT_REFUSE_REINFORCE=7;
	/*
	 * 8:甲方同意乙方退回
	 */
	public static Integer FAULT_CONSENT_RETURN=8;
	/*
	 * 9:甲方重新发起报障单
	 */
	public static Integer FAULT_REISSUE=9;
	/*
	 * 10:乙方请求转派
	 */
	public static Integer FAULT_REQUEST_REDEPLOY=10;
	/*
	 * 11:填写工单 
	 */
	public static Integer FAULT_SAVE_WORD_ORDER=11;
	/*
	 * 12:无需工单
	 */
	public static Integer FAULT_NO_WORD_ORDER=12;
	/*
	 * 13:工单不通过
	 */
	public static Integer FAULT_NOT_PASSE_WORD_ORDER =13;
	/*
	 * 14:工单通过 
	 */
	public static Integer FAULT_PASSE_WORD_ORDER=14;
	/*
	 * 15:请求增援
	 */
	public static Integer FAULT_REQUEST_REINFORCE=15;
	/*
	 * 16:维修说明
	 */
	public static Integer FAULT_SAVE_MAINTAIN_INFO=16;
	/*
	 * 17:完成维修
	 */
	public static Integer FAULT_FINISH_MAINTAIN=17; 
	/*
	 * 18:验收合格 
	 */
	public static Integer FAULT_QUALIFIED=18;
	/*
	 * 19:验收不合格  
	 */
	public static Integer FAULT_DISQUALIFICATION=19;
	/*
	 * 20:工单评价
	 */
	public static Integer FAULT_EVALUATE=20;
	
	public static HashMap<Integer, String> FAULT_INFO_MAP(){
		HashMap<Integer, String> MAP = new HashMap<Integer, String>();
//		MAP.put(FAULT_INFO_STATE.ALL,FAULT_INFO_STATE_STR.ALL);
//		MAP.put(FAULT_INFO_STATE.WAIT_RECEIVE,FAULT_INFO_STATE_STR.WAIT_RECEIVE);
//		MAP.put(FAULT_INFO_STATE.WAIT_FILL_ORDER,FAULT_INFO_STATE_STR.WAIT_FILL_ORDER);
//		MAP.put(FAULT_INFO_STATE.WAIT_CONFIRM,FAULT_INFO_STATE_STR.WAIT_CONFIRM);
//		MAP.put(FAULT_INFO_STATE.MAINTAINING,FAULT_INFO_STATE_STR.MAINTAINING);
//		MAP.put(FAULT_INFO_STATE.ORDER_REFUSE,FAULT_INFO_STATE_STR.ORDER_REFUSE);
//		MAP.put(FAULT_INFO_STATE.WAIT_ACCEPTANCE,FAULT_INFO_STATE_STR.WAIT_ACCEPTANCE);
//		MAP.put(FAULT_INFO_STATE.WAIT_EVALUATE,FAULT_INFO_STATE_STR.WAIT_EVALUATE);
//		MAP.put(FAULT_INFO_STATE.FINISH,FAULT_INFO_STATE_STR.FINISH);
//		MAP.put(FAULT_INFO_STATE.CLOSE,FAULT_INFO_STATE_STR.CLOSE);
//		MAP.put(FAULT_INFO_STATE.REFUSE,FAULT_INFO_STATE_STR.REFUSE);
//		MAP.put(FAULT_INFO_STATE.REVOKE,FAULT_INFO_STATE_STR.REVOKE);
//		MAP.put(FAULT_INFO_STATE.REVOKE_ORDER_REFUSE,FAULT_INFO_STATE_STR.REVOKE_ORDER_REFUSE);
//		MAP.put(FAULT_INFO_STATE.REVOKE_WAIT_CONFIRM ,FAULT_INFO_STATE_STR.REVOKE_WAIT_CONFIRM );
		return MAP;
	}
	//报障紧急程度
	public interface LEVEL{
		public static int NORMAL= 0;//一般
		public static int URGEN= 1;//紧急
		public static int NOW= 2;//立刻
	}
	public interface LEVEL_STR{
		public static String NORMAL= "一般";
		public static String URGEN= "紧急";
		public static String NOW= "立刻";
	}
	public static HashMap<Integer, String> LEVEL_MAP(){
		HashMap<Integer, String> MAP = new HashMap<Integer, String>();
		MAP.put(LEVEL.NORMAL,LEVEL_STR.NORMAL);
		MAP.put(LEVEL.URGEN,LEVEL_STR.URGEN);
		MAP.put(LEVEL.NOW,LEVEL_STR.NOW);
		return MAP;
	}
	
	/*
		消息类型，
		0系统消息  
		1邀请甲方负责人
		2邀请乙方负责人 
		3邀请普通成员进团队  
		4甲方邀请普通成员进项目 
		5乙方邀请普通成员进项目 
		6邀请成为团队负责人 
		7邀请成为项目负责人
		8申请加入企业 
		9 退出项目
		10申请加入项目
		11项目相关 
		12其他消息 
	 */
	public interface MESSAGE_TYPE_INDEX{
		/**
		 * 0系统消息 
		 */
		public static Integer SYSTEM_INDEX = 0;
		/**
		 * 1邀请甲方负责人
		 */
		public static Integer INVITED_A_PERSON_INDEX = 1;
		/**
		 * 2邀请乙方负责人 
		 */
		public static Integer INVITED_B_PERSON_INDEX = 2;
		/**
		 * 3邀请普通成员进团队  
		 */
		public static Integer INVITED_PEOPLE_JOIN_COMPANY_INDEX = 3;
		/**
		 * 4甲方邀请普通成员进项目 
		 */
		public static Integer A_INVITED_PEOPLE_JOIN_PROJECT_INDEX = 4;
		/**
		 * 5乙方邀请普通成员进项目 
		 */
		public static Integer B_INVITED_PEOPLE_JOIN_PROJECT_INDEX = 5;
		/**
		 * 6邀请成为团队负责人 
		 */
		public static Integer INVITED_PEOPLE_CHANGE_COMPANY_PERSON_INDEX = 6;
		/**
		 * 7邀请成为项目负责人   暂时无用
		 */
		public static Integer INVITED_PEOPLE_CHANGE_PROJECT_PERSON_INDEX=7;
		/**
		 * 8申请加入企业 
		 */
		public static Integer APPLY_JOIN_COMPANY_INDEX = 8;
		/**
		 * 9 退出项目
		 */
		public static Integer QUIT_PROJECT = 9;
		
		/**
		 * 10加入项目
		 */
		public static Integer APPLY_JOIN_PROJECT_INDEX = 10;
		/**
		 * 11项目相关 
		 */
		public static Integer PROJECT_INDEX = 11;
		/**
		 * 12其他消息 
		 */
		public static Integer OTHER_INDEX = 12;
		/**
		 * 13离开团队
		 */
		public static Integer LEFT_COMPANY=13;
	}
	
	public interface MESSAGE_TYPE_STR{
		public static String SYSTEM_STR= "系统消息";
		public static String INVITED_A_PERSON_STR = "邀请甲方负责人";
		public static String INVITED_B_PERSON_STR = "邀请乙方负责人";
		public static String INVITED_PEOPLE_JOIN_COMPANY_STR = "邀请普通成员进团队";
		public static String A_INVITED_PEOPLE_JOIN_PROJECT_STR = "甲方邀请普通成员进项目";
		public static String B_INVITED_PEOPLE_JOIN_PROJECT_STR = "乙方邀请普通成员进项目";
		public static String INVITED_PEOPLE_CHANGE_COMPANY_PERSON_STR = "邀请成为团队负责人";
		public static String INVITED_PEOPLE_CHANGE_PROJECT_PERSON_STR = "邀请成为项目负责人";
		public static String APPLY_JOIN_COMPANY_STR = "申请加入企业";
		public static String PROJECT_STR = "项目相关";
		public static String OTHER_STR = "其他相关";
	}
	
	public static HashMap<Integer, String> MESSAGE_TYPE_MAP(){
		HashMap<Integer, String> MAP = new HashMap<Integer, String>();
		MAP.put(MESSAGE_TYPE_INDEX.SYSTEM_INDEX,MESSAGE_TYPE_STR.SYSTEM_STR);
		MAP.put(MESSAGE_TYPE_INDEX.INVITED_A_PERSON_INDEX,MESSAGE_TYPE_STR.INVITED_A_PERSON_STR);
		MAP.put(MESSAGE_TYPE_INDEX.INVITED_B_PERSON_INDEX,MESSAGE_TYPE_STR.INVITED_B_PERSON_STR);
		MAP.put(MESSAGE_TYPE_INDEX.INVITED_PEOPLE_JOIN_COMPANY_INDEX,MESSAGE_TYPE_STR.INVITED_PEOPLE_JOIN_COMPANY_STR);
		MAP.put(MESSAGE_TYPE_INDEX.A_INVITED_PEOPLE_JOIN_PROJECT_INDEX,MESSAGE_TYPE_STR.A_INVITED_PEOPLE_JOIN_PROJECT_STR);
		MAP.put(MESSAGE_TYPE_INDEX.B_INVITED_PEOPLE_JOIN_PROJECT_INDEX,MESSAGE_TYPE_STR.B_INVITED_PEOPLE_JOIN_PROJECT_STR);
		MAP.put(MESSAGE_TYPE_INDEX.INVITED_PEOPLE_CHANGE_COMPANY_PERSON_INDEX,MESSAGE_TYPE_STR.INVITED_PEOPLE_CHANGE_COMPANY_PERSON_STR);
		MAP.put(MESSAGE_TYPE_INDEX.INVITED_PEOPLE_CHANGE_PROJECT_PERSON_INDEX,MESSAGE_TYPE_STR.INVITED_PEOPLE_CHANGE_PROJECT_PERSON_STR);
		MAP.put(MESSAGE_TYPE_INDEX.APPLY_JOIN_COMPANY_INDEX,MESSAGE_TYPE_STR.APPLY_JOIN_COMPANY_STR);
		MAP.put(MESSAGE_TYPE_INDEX.PROJECT_INDEX,MESSAGE_TYPE_STR.PROJECT_STR);
		MAP.put(MESSAGE_TYPE_INDEX.OTHER_INDEX,MESSAGE_TYPE_STR.OTHER_STR);
		return MAP;
	}
	
	
	//项目人员类型
	//0甲方
	//1乙方
	public interface PROJECT_USER_TYPE_INDEX{
		public static Integer A_INDEX = 0;
		public static Integer B_INDEX = 1;
	}
	
	public interface PROJECT_USER_TYPE_STR{
		public static String A_STR = "甲方";
		public static String B_STR = "乙方";
	}
	
	public static HashMap<Integer, String> PROJECT_USER_TYPE_MAP(){
		HashMap<Integer, String> MAP = new HashMap<Integer, String>();
		MAP.put(PROJECT_USER_TYPE_INDEX.A_INDEX,PROJECT_USER_TYPE_STR.A_STR);
		MAP.put(PROJECT_USER_TYPE_INDEX.B_INDEX,PROJECT_USER_TYPE_STR.B_STR);
		return MAP;
	}
	
	//消息类别
	public interface MESSAGE_CLASSIFY_INDEX{
		public static Integer COMPANY = 0;//企业类
		public static Integer PROJECT = 1;//项目类
		public static Integer MAINTAIN = 2;//报障类
		public static Integer OTHERLOGIN = 3;//异地登录
		public static Integer PATROL = 4;//报障类
	}
	
	//维修内容状态
	/*
	 * 状态 0不可查看详情  1查看保障单 2查看评价  3查看工单 4查看维修 5验收不合格查看原因
	 */
	public interface MAINTAIN_STATE_INDEX{
		public static Integer NO_DETAIL = 0;//不可查看详情
		public static Integer LOOK_DETAIL = 1;//查看保障单
		public static Integer LOOK_EVALUATE = 2;//查看评价
		public static Integer LOOK_WORK_ORDER = 3;//查看工单
		public static Integer LOOK_MAINTAIN_DETAIL = 4;//查看维修
		public static Integer LOOK_CHECK_DETAIL = 5;//查看编辑的保障详情
	}
	//推送消息类别
	public interface PUSH_TYPE_INDEX{
		public static Integer SINGLE_LOGIN = 0;//单点登录
		public static Integer NORMAL_MESSAGE = 1;//普通消息
	}
	
	
	//消息状态
	public interface MESSAGE_STATE_INDEX{
		/**
		 * 0 待处理
		 */
		public static Integer PENDING = 0;//待处理
		/**
		 * 1 已同意
		 */
		public static Integer AGREE = 1;//已同意
		/**
		 * 2 已拒绝
		 */
		public static Integer REFUSE = 2;//已拒绝
		/**
		 * 3 不用处理
		 */
		public static Integer NO_HANDLE = 3;//不用处理
	}
	
	//项目状态
	public interface PROJECT_STATE_INDEX{
		public static Integer NO_CONFIRM = 0;//未确认
		public static Integer CONFIRM = 1;//已确认（生效）
	}
	//维修人员类型
	public interface MAINTAIN_USER_TYPE{
		public static Integer RESPONSER = 0;//负责人
		public static Integer SENDER = 1;//转派人员
		public static Integer REINFORCE = 2; //增援人员
	}
	
	
	//服务类型状态
	public interface SERVICE_TYPE_STATE_INDEX{
		public static Integer PENDING = 0;//待确认
		public static Integer AGREE = 1;//已确认
		public static Integer REFUSE = 2;//已拒绝
	}
	public interface SERVICE_TYPE_STATE_STR{
		public static String PENDING = "待确认";
		public static String AGREE = "已确认";
		public static String REFUSE = "已拒绝";
	}
	public static HashMap<Integer, String> SERVICE_TYPE_STATE_MAP(){
		HashMap<Integer, String> MAP = new HashMap<Integer, String>();
		MAP.put(SERVICE_TYPE_STATE_INDEX.PENDING,SERVICE_TYPE_STATE_STR.PENDING);
		MAP.put(SERVICE_TYPE_STATE_INDEX.AGREE,SERVICE_TYPE_STATE_STR.AGREE);
		MAP.put(SERVICE_TYPE_STATE_INDEX.REFUSE,SERVICE_TYPE_STATE_STR.REFUSE);
		return MAP;
	}
	
}
