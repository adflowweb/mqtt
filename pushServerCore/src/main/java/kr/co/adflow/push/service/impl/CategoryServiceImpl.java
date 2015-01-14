/*
 * 
 */
package kr.co.adflow.push.service.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.dao.CategoryDao;
import kr.co.adflow.push.domain.Category;
import kr.co.adflow.push.service.CategoryService;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class CategoryServiceImpl.
 *
 * @author nadir93
 * @date 2014. 7. 7.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(CategoryServiceImpl.class);

	/** The category dao. */
	@Resource
	CategoryDao categoryDao;

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.service.CategoryService#get(int)
	 */
	@Override
	public Category get(int categoryID) throws Exception {
		logger.debug("get시작(categoryID=" + categoryID + ")");
		Category category = categoryDao.get(categoryID);
		logger.debug("get종료(category=" + category + ")");
		return category;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.service.CategoryService#post(kr.co.adflow.push.domain.Category)
	 */
	@Override
	public int post(Category category) throws Exception {
		logger.debug("post시작(category=" + category + ")");
		int count = categoryDao.post(category);
		logger.debug("post종료(updates=" + count + ")");
		return count;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.service.CategoryService#delete(int)
	 */
	@Override
	public int delete(int categoryID) throws Exception {
		logger.debug("delete시작(categoryID=" + categoryID + ")");
		int count = categoryDao.delete(categoryID);
		logger.debug("delete종료(updates=" + count + ")");
		return count;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.service.CategoryService#getAllCategories()
	 */
	@Override
	public Category[] getAllCategories() throws Exception {
		logger.debug("getAllCategories시작()");
		Category[] category = categoryDao.getAllCategories();
		logger.debug("getAllCategories종료(category=" + category + ")");
		return category;
	}

}
