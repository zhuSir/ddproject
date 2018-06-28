package com.xmsmartcity.maintain.controller.mobile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.*;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import com.xmsmartcity.maintain.pojo.Company;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.CompanyService;
import com.xmsmartcity.maintain.service.UserService;
import com.xmsmartcity.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xmsmartcity.maintain.pojo.Equips;
import com.xmsmartcity.maintain.pojo.EquipsType;
import com.xmsmartcity.maintain.service.EquipsService;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/api/equips")
public class EquipsApiController {
	
	@Autowired
	private EquipsService equipsService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private UserService userService;
	
	/**
	 * 创建设备
	 * @param userId
	 * @param token
	 * @param equips
	 * @return
	 */
	@RequestMapping(value = "/create-equips")
	@ResponseBody
	public String createEquips(Integer userId, String token, Equips equips) {
		equips.setCreatUserId(userId);
		Date now =new Date();
		equips.setCreattime(now);
		Assert.state(equips !=null,Constant.PARAMS_ERROR);
		Assert.state(StringUtils.isNotBlank(equips.getEquipName()) && StringUtils.isNotBlank(equips.getEquipModel()) && equips.getEquipTypeId() !=null && StringUtils.isNotBlank(equips.getFirm()),Constant.PARAMS_ERROR);
		equips.setCreatUserId(userId);
		Equips res = equipsService.saveOrUpdateEquips(equips);
		return Utils.toSuccessJsonResults(res);
	}
	
	/**
	 * 更新设备
	 * @param userId
	 * @param token
	 * @param equips
	 * @return
	 */
	@RequestMapping(value = "/update-equips")
	@ResponseBody
	public String updateEquips(Integer userId, String token, Equips equips) {
		Date now =new Date();
		Assert.state(equips !=null,Constant.PARAMS_ERROR);
		Assert.state(equips.getId()!=null && StringUtils.isNotBlank(equips.getEquipName()) && StringUtils.isNotBlank(equips.getEquipModel()) && equips.getEquipTypeId() !=null && StringUtils.isNotBlank(equips.getFirm()),Constant.PARAMS_ERROR);
		Assert.state(StringUtils.isNotBlank(equips.getId().toString()),"设备ID有误，请刷新后重试！");
		Equips oldEquip=equipsService.selectByPrimaryKey(equips.getId());
		Assert.state(oldEquip !=null,"设备ID有误，请刷新后重试！");
		Assert.state(userId.toString().trim().equals(oldEquip.getCreatUserId().toString().trim()),"只有创建人才有权限编辑");
		oldEquip.setEquipName(equips.getEquipName());
		oldEquip.setEquipDetail(equips.getEquipDetail());
		oldEquip.setEquipModel(equips.getEquipModel());
		oldEquip.setEquipTypeId(equips.getEquipTypeId());
		oldEquip.setProvince(equips.getProvince());
		oldEquip.setCity(equips.getCity());
		oldEquip.setArea(equips.getArea());
		oldEquip.setAddress(equips.getAddress());
		oldEquip.setLat(equips.getLat());
		oldEquip.setLng(equips.getLng());
		oldEquip.setFirm(equips.getFirm());
		oldEquip.setUpdatetime(now);
		oldEquip.setUpdateUserId(userId);
		oldEquip.setAddress(equips.getAddress());
		oldEquip.setArea(equips.getArea());
		oldEquip.setCity(equips.getCity());
		oldEquip.setProvince(equips.getProvince());
		Equips res = equipsService.saveOrUpdateEquips(oldEquip);
		return Utils.toSuccessJsonResults(res);
	}
	
	/**
	 * 查询设备（根据ID等字段）
	 * @param userId
	 * @param token
	 * @param equips
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/select-equips")
	@ResponseBody
	public String selectEquips(Integer userId, String token,Equips equips) throws Exception {
		Assert.state(equips.getId() !=null,Constant.PARAMS_ERROR);
		List<Equips> list=equipsService.getEquipsList(null,null,equips,userId);
		if (list.size()==0) {
			throw new Exception("找不到该设备，请刷新后重试");
		}
		float avgScore=equipsService.getEquipsAvg(equips.getId());
		float sumTime=equipsService.getEquipsSumTime(equips.getId());
		Equips res=list.get(0);
		res.setAvgScore(String.valueOf(avgScore));
		res.setSumTime(String.valueOf(sumTime));
		return Utils.toSuccessJsonResults(res);
	}
	
	/**
	 * 查询设备列表（app使用）
	 * @param userId
	 * @param token
	 * @param startIndex
	 * @param pageSize
	 * @param name
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value = "/select-equipsList")
	@ResponseBody
	public String selectEquipsList(Integer userId, String token,Integer startIndex,
		Integer pageSize,Integer projectId,Equips equips) {
		equips.setServiceTypeId(projectId);
		List<Equips> equipList=equipsService.getEquipsList(startIndex, pageSize, equips,userId);
		return Utils.toSuccessJson(equipList);
	}
	
	/**
	 * 删除设备
	 * @param userId
	 * @param token
	 * @param equips
	 * @return
	 */
	@RequestMapping(value = "/delete-equips")
	@ResponseBody
	public String deleteEquips(Integer userId, String token, Equips equips) {
		equips.setUpdateUserId(userId);
		Date now =new Date();
		equips.setUpdatetime(now);
		Assert.state(equips !=null,Constant.PARAMS_ERROR);
		Assert.state(StringUtils.isNotBlank(equips.getId().toString()),"设备ID有误，请刷新后重试！");
		equipsService.deleteEquips(equips);
		return Utils.toSuccessJson("删除成功！");
	}
	
	/**
	 * 查询设备类别列表
	 * @param userId
	 * @param token
	 * @param startIndex
	 * @param pageSize
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/select-equipsType")
	@ResponseBody
	public String selectEquipsTypeList(Integer userId, String token,Integer startIndex,
		Integer pageSize,String name) {
		List<EquipsType> equipsTypes=equipsService.selectEquipsTypeList(userId,startIndex, pageSize,name);
		return Utils.toSuccessJson(equipsTypes);
	}
	
	/**
	 * 根据设备查询项目
	 * @param userId
	 * @param token
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/select-projectByEquip")
	@ResponseBody
	public String selectProjectByEquip(Integer userId, String token,Integer id) {
		Map<String, Object> map=equipsService.selectProjectByEquip(id,userId);
		return Utils.toSuccessJsonResults(map);
	}
	
	/**
	 * 设备概览统计
	 * @param userId
	 * @param token
	 * @param id
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/select-summaryByEquip")
	@ResponseBody
	public String selectSummaryByEquip(Integer userId, String token,Integer id,Integer startIndex,Integer pageSize) {
		if (startIndex!=null && pageSize!=null) {
			startIndex=startIndex*pageSize;
		}
		List<Map<String, Object>> list = equipsService.getProcessingByEquip(userId, id,startIndex,pageSize);
		Map<String, Object> suMap=equipsService.getSummaryByEquip(userId, id);
		suMap.put("faultList", list);
		return Utils.toSuccessJsonResults(suMap);
	}
	
	/**
	 * 根据设备编号查询设备
	 * @param equipCode
	 * @return
	 */
	@RequestMapping(value = "/select-equipByCode")
	@ResponseBody
	public String selectequipByCode(String equipCode) {
		Assert.state(equipCode !=null,Constant.PARAMS_ERROR);
		Map<String, Object> record = equipsService.getServiceByEquipCode(equipCode);
		return Utils.toSuccessJsonResults(record);
	}
	
	/**
	 * 创建设备类别
	 * @param userId
	 * @param token
	 * @param equipsType
	 * @return
	 */
	@RequestMapping(value = "/create-equipsType")
	@ResponseBody
	public String createEquipsType(Integer userId, String token, EquipsType equipsType) {
		Assert.state(equipsType !=null,Constant.PARAMS_ERROR);
		Assert.state(StringUtils.isNotBlank(equipsType.getEquipsTypeName()) ,Constant.PARAMS_ERROR);
		EquipsType res = equipsService.saveOrUpdateEquipsType(equipsType,userId);
		return Utils.toSuccessJsonResults(res);
	}
	
	/**
	 * 更新设备类别
	 * @param userId
	 * @param token
	 * @param equipsType
	 * @return
	 */
	@RequestMapping(value = "/update-equipsType")
	@ResponseBody
	public String updateEquipsType(Integer userId, String token, EquipsType equipsType) {
		Assert.state(equipsType !=null && equipsType.getId()!=null && equipsType.getId()!=0,Constant.PARAMS_ERROR);
		Assert.state(StringUtils.isNotBlank(equipsType.getEquipsTypeName()),Constant.PARAMS_ERROR);
		EquipsType res = equipsService.saveOrUpdateEquipsType(equipsType,userId);
		return Utils.toSuccessJsonResults(res);
	}

	/**
	 * 删除设备类别
	 * @param userId
	 * @param token
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/del-equipsType")
	@ResponseBody
	public String deleteEquipsType(Integer userId, String token, Integer id) {
		Assert.state(id != null && id != 0,Constant.PARAMS_ERROR);
		equipsService.deleteEquipsType(id);
		return Utils.toSuccessJson("删除成功！");
	}

	/**
	 * 批量导入设备
	 * @param userId
	 * @param token
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/read-excel")
	@ResponseBody
	@Transactional
	public String readExcel(Integer userId, String token, MultipartFile file)throws Exception {
		Assert.state(file != null,Constant.PARAMS_ERROR);
		List<ArrayList<String>> lists=ExcelUtil.readXlsx(file);
		Date date=new Date();
		User user=userService.selectByPrimaryKey(userId);
		Company company=companyService.selectByPrimaryKey(user.getCompanyId());
		String PYname= Util.getUpperPinYinHeadChar(company==null?user.getName():company.getName());
		String prefix=PYname+company.getId();
		Integer code=equipsService.getNewCode(prefix);
		DecimalFormat df = new DecimalFormat("00000");
		List<Equips> equipsList=new ArrayList<>();//新增的设备
		List<HashMap<String,String>> typeNameList=new ArrayList<>();
		List<EquipsType> insertList=new ArrayList<>();//新增插入的设备类别
		List<String> insertEquipTypeNames=new ArrayList<>();
		List<EquipsType> oldEquipsTypeList=equipsService.selectEquipsTypeList(userId,null,null,null);

		//提取需插入设备内的设备类别名称
		for (ArrayList<String> item:lists){
			String typeName=item.get(8);
			if (!typeNameList.contains(typeName)){
				HashMap<String,String> map =new HashMap<>();
				map.put("typeName",typeName);
				typeNameList.add(map);
			}
		}

		//查找设备类别是否已存在
		for (HashMap<String,String> map:typeNameList) {
			boolean isExist=false;
			for (EquipsType equipsType:oldEquipsTypeList){
				String name=map.get("typeName");
				if (name.equals(equipsType.getEquipsTypeName())){
					isExist=true;
					break;
				}
			}
			if (!isExist) {
				insertEquipTypeNames.add(map.get("typeName"));
			}
		}

		//插入不存在的设备类别
		if (insertEquipTypeNames.size()>0){
			for (String name:insertEquipTypeNames) {
				EquipsType equipsType=new EquipsType();
				equipsType.setCreatetime(new Date());
				equipsType.setCreateUserId(userId);
				equipsType.setEquipsTypeName(name);
				insertList.add(equipsType);
			}
			equipsService.insertEquipTypeBatch(insertList);
		}
		//新的设备类别
		List<EquipsType> equipsTypes=equipsService.selectEquipsTypeList(userId,null,null,null);
//		String url="http://apis.map.qq.com/ws/geocoder/v1/?key=UIGBZ-G26WX-65545-7DJWP-CG37V-M5BVA&address=";
		for (ArrayList<String> item:lists) {
			Equips equips=new Equips();
			equips.setEquipName(item.get(0));
			equips.setEquipModel(item.get(1));
			equips.setFirm(item.get(2));
			equips.setProvince(item.get(3));
			equips.setCity(item.get(4));
			equips.setArea(item.get(5));
			equips.setAddress(item.get(6));
			equips.setEquipDetail(item.get(7));
			equips.setCreattime(date);
			equips.setCreatUserId(userId);
			String equipCode=prefix+df.format(code);
			equips.setEquipCode(equipCode);
//			String postUrl=url+item.get(3)+item.get(4)+item.get(5)+item.get(6);
//			JSONObject jsonObject=HttpUtil.href(postUrl);
			for (int i=0;i<equipsTypes.size();i++){
				if (item.get(8).toString().equals(equipsTypes.get(i).getEquipsTypeName())){
					equips.setEquipTypeId(equipsTypes.get(i).getId());
					break;
				}
			}
			code++;
			equips.setIsDel(0);
			equipsList.add(equips);
		}
		if (equipsList.size()>0) {
			equipsService.insertEquipBatch(equipsList);			
		}
		return Utils.toSuccessJson("插入成功！");
	}
	
	/**
	 * 二维码下载
	 * @param equipId
	 * @param width
	 * @param height
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping(value="/export-qrcode")
	@ResponseBody
	public void exportQrcode(Integer id,Integer width,Integer height,HttpServletResponse response,HttpServletRequest request)throws IOException{
		try {
			Equips equips=equipsService.selectByPrimaryKey(id);
			Assert.isTrue(equips!=null, "查无此设备");
			HashMap<String, Object> map=new HashMap<String,Object>();
			map.put("name", equips.getEquipName());
			String codeStr="http://maintain.dweibao.com/dweibao/xcx/qr?code="+equips.getEquipCode();	
			BufferedImage image=null;
			image=QRCodeHelper.getQrcodeImageByLogo(codeStr,request,map);
			response.setContentType("image/png");  
			OutputStream os = response.getOutputStream(); 
			ImageIO.write(image, "png", os);
			os.flush();  
			os.close(); 
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
		
	/**
	 * 通过服务ID生成设备列表
	 * @param serviceTypeId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/xcx/select-ByServiceTypeId")
	@ResponseBody
	public String getByServiceTypeId(Integer serviceTypeId,Integer startIndex,Integer pageSize) {
		if (startIndex!=null && pageSize!=null) {
			startIndex=startIndex*pageSize;
		}
		if (serviceTypeId==null || serviceTypeId==0) {
			return Utils.toSuccessJson(new ArrayList<>());
		}
		List<Equips> equips = equipsService.getByServiceTypeId(serviceTypeId, startIndex, pageSize);
		return Utils.toSuccessJson(equips);
	}
	
	/**
	 * 通过搜索设备列表
	 * @param equipName
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/xcx/listBySearch")
	@ResponseBody
	public String xcxListBySearch(String searchName,Integer serviceTypeId,Integer startIndex,Integer pageSize) throws IOException{
		if (startIndex==null || pageSize==null) {
			return Utils.toFailJson("参数错误",null);
		}
		if (serviceTypeId==null || serviceTypeId==0) {
			return Utils.toSuccessJson(new ArrayList<>());
		}
		if (searchName!=null && !searchName.equals("")) {
			searchName=URLDecoder.decode(searchName,"UTF-8");
		}
		List<Equips> equips = equipsService.listBySearch(searchName,serviceTypeId, startIndex, pageSize);
		return Utils.toSuccessJson(equips);
	}
}
