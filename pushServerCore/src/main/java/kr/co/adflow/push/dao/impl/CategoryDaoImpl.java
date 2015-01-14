/*
 * 
 */
package kr.co.adflow.push.dao.impl;

import kr.co.adflow.push.dao.CategoryDao;
import kr.co.adflow.push.domain.Category;
import kr.co.adflow.push.mapper.CategoryMapper;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

// TODO: Auto-generated Javadoc
/**
 * The Class CategoryDaoImpl.
 *
 * @author nadir93
 * @date 2014. 7. 7.
 */
@Repository
public class CategoryDaoImpl implements CategoryDao {

	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(CategoryDaoImpl.class);

	/** The sql session. */
	@Autowired
	private SqlSession sqlSession;

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.dao.CategoryDao#get(int)
	 */
	@Override
	public Category get(int categoryID) throws Exception {
		logger.debug("get시작(categoryID=" + categoryID + ")");
		CategoryMapper categoryMapper = sqlSession
				.getMapper(CategoryMapper.class);
		Category category = categoryMapper.get(categoryID);
		logger.debug("get종료(" + category + ")");
		return category;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.dao.CategoryDao#post(kr.co.adflow.push.domain.Category)
	 */
	@Override
	public int post(Category category) throws Exception {
		logger.debug("post시작(category=" + category + ")");
		CategoryMapper categoryMapper = sqlSession
				.getMapper(CategoryMapper.class);
		int count = categoryMapper.post(category);
		logger.debug("post종료(count=" + count + ")");
		return count;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.dao.CategoryDao#delete(int)
	 */
	@Override
	public int delete(int categoryID) throws Exception {
		logger.debug("delete시작(categoryID=" + categoryID + ")");
		CategoryMapper categoryMapper = sqlSession
				.getMapper(CategoryMapper.class);
		int result = categoryMapper.delete(categoryID);
		logger.debug("delete종료(result=" + result + ")");
		return result;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.dao.CategoryDao#getAllCategories()
	 */
	@Override
	public Category[] getAllCategories() throws Exception {
		logger.debug("getAllCategories시작()");
		CategoryMapper categoryMapper = sqlSession
				.getMapper(CategoryMapper.class);
		Category[] category = categoryMapper.getAllCategories();
		logger.debug("getAllCategories종료(category=" + category + ")");
		return category;
	}

}
