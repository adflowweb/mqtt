/*
 * 
 */
package kr.co.adflow.push.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import kr.co.adflow.push.domain.Category;
import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Result;
import kr.co.adflow.push.service.CategoryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

// TODO: Auto-generated Javadoc
/**
 * The Class CategoryController.
 *
 * @author nadir93
 * @date 2014. 7. 7.
 */
@Controller
public class CategoryController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(CategoryController.class);

	/** The category service. */
	@Resource
	private CategoryService categoryService;

	/**
	 * 카테고리 등록하기.
	 *
	 * @param category the category
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "categories", method = RequestMethod.POST)
	@ResponseBody
	public Response post(@RequestBody Category category) throws Exception {
		logger.debug("카테고리=" + category);
		final int count = categoryService.post(category);
		Result result = new Result();
		result.setSuccess(true);
		List<String> messages = new ArrayList<String>() {
			{
				add("updates=" + count);
			}
		};
		result.setInfo(messages);
		Response res = new Response(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 카테고리 삭제하기.
	 *
	 * @param categoryID the category id
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "categories/{categoryID}", method = RequestMethod.DELETE)
	@ResponseBody
	public Response delete(@PathVariable int categoryID) throws Exception {
		logger.debug("카테고리번호=" + categoryID);
		final int count = categoryService.delete(categoryID);
		Result result = new Result();
		result.setSuccess(true);
		List<String> messages = new ArrayList<String>() {
			{
				add("updates=" + count);
			}
		};
		result.setInfo(messages);
		Response res = new Response(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 카테고리 가져오기.
	 *
	 * @param categoryID the category id
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "categories/{categoryID}", method = RequestMethod.GET)
	@ResponseBody
	public Response<Category> get(@PathVariable int categoryID)
			throws Exception {
		logger.debug("categoryID=" + categoryID);
		Result<Category> result = new Result<Category>();
		result.setSuccess(true);
		Category category = categoryService.get(categoryID);
		logger.debug("category=" + category);
		if (category == null) {
			List<String> messages = new ArrayList<String>() {
				{
					add("category not found");
				}
			};
			result.setInfo(messages);
		} else {
			result.setData(category);
		}
		Response<Category> res = new Response<Category>(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 전체 카테고리 가져오기.
	 *
	 * @return the msgs
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "categories", method = RequestMethod.GET)
	@ResponseBody
	public Response<Category[]> getMsgs() throws Exception {
		Category[] category = categoryService.getAllCategories();
		logger.debug("category=" + category);
		Result<Category[]> result = new Result<Category[]>();
		result.setSuccess(true);
		result.setData(category);
		Response<Category[]> res = new Response<Category[]>(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 예외처리.
	 *
	 * @param e the e
	 * @return the response
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Response handleAllException(final Exception e) {
		logger.error("예외발생", e);
		Result result = new Result();
		result.setSuccess(false);
		List<String> messages = new ArrayList<String>() {
			{
				add(e.toString());
				// add(e.getMessage());
				// add("are u.");
			}
		};
		result.setErrors(messages);
		Response res = new Response(result);
		return res;
	}

}
