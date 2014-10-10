package com.qait.happyhours.rest.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.qait.happyhours.service.DealService;
import com.qait.happyhours.util.HappyHoursUtil;

@Controller
public class UploadImageController {

	@Autowired
	private DealService dealService;
	
/*	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public String addLogo(@RequestParam("image") MultipartFile file, HttpServletRequest request) {
		 try {
             if (!file.isEmpty()) {
                 byte[] bytes = file.getBytes();
                 // store the bytes somewhere
                 return "redirect:uploadSuccess";
             } else {
                 return "redirect:uploadFailure";
             }
         }
         catch (Exception ex)
         {

         }
		return "redirect:/adminHome.htm";
	}*/
	
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public String addLogo(@RequestParam("image") MultipartFile file,
			@RequestParam(value = "dealID", required = false) long dealID,
			@RequestParam(value = "offerID", required = false) long offerID, HttpServletRequest request) {
		
		try {
			if (!file.isEmpty()) {
				byte[] bytes = file.getBytes();
				//HappyHoursUtil.uploadImageOnServer(file, dealID, offerID);
			} else {
				return "redirect:uploadFailure";
			}
		} catch (Exception ex) {

		}
		return "redirect:/adminHome.htm";
	}
	
/*	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public String test(HttpServletRequest request) {
		
		try {
			if (!file.isEmpty()) {
				byte[] bytes = file.getBytes();
				HappyHoursUtil.uploadImageOnServer(file, dealID, offerID);
			} else {
				return "redirect:uploadFailure";
			}
		} catch (Exception ex) {

		}
		return "redirect:/adminHome.htm";
	}*/

	public DealService getDealService() {
		return dealService;
	}

	public void setDealService(DealService dealService) {
		this.dealService = dealService;
	}
}
