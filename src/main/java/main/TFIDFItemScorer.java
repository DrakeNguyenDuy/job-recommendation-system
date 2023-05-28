package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;
import org.grouplens.lenskit.vectors.similarity.CosineVectorSimilarity;
import org.lenskit.api.ResultMap;
import org.lenskit.basic.AbstractItemScorer;
import org.lenskit.data.dao.PrefetchingUserEventDAO;
import org.lenskit.data.dao.UserEventDAO;
import org.lenskit.data.events.Like;
import org.lenskit.inject.Transient;
import org.lenskit.results.BasicResult;
import org.lenskit.results.BasicResultMap;

import contants.IndexConstants;
import dao.UserDao;
import utils.ScoreUtils;

public class TFIDFItemScorer extends AbstractItemScorer {
	private UserEventDAO dao;
	private TFIDFModel model;
	private UserDao userDao;

	@Inject
	public TFIDFItemScorer(PrefetchingUserEventDAO dao, TFIDFModel model, UserDao userDao) {
		super();
		this.dao = dao;
		this.model = model;
		this.userDao = userDao;
	}

	@Override
	public ResultMap scoreWithDetails(long user, Collection<Long> items) {
		List<BasicResult> list = new ArrayList<BasicResult>();
		SparseVector userVector = makeUserVector(user);
		System.out.println("vetoruser");
		System.out.println(userVector);
		System.out.println();
		CosineVectorSimilarity cvs = new CosineVectorSimilarity();
		for (Long idItem : items) {
			SparseVector vectorItem = model.getVectorItem(idItem);
//			System.out.println(vectorItem);
			double similar = cvs.similarity(userVector, vectorItem);
			list.add(new BasicResult(idItem, similar));
		}

		return new BasicResultMap(list);
	}

	private SparseVector makeUserVector(long user) {
		SparseVector userVector = null;
		List<Like> userLikes = dao.getEventsForUser(user, Like.class);
		if (userLikes == null) {
			return createVectorUserFromFeatures(user);
		}
		return userVector;

	}

	// Create user vector from feature user like skills, address,...
	// Used to create user vector when user not like any item
	private SparseVector createVectorUserFromFeatures(long idUser) {
		List<String> featureUser = userDao.getFeatureUser(idUser);
		// vector profile user
		MutableSparseVector userVector = model.createVectorFeatures();
		userVector.fill(0.0);
		// Compute TF-IDF all features in profile user

		// get map id feature -> name feature
		Map<String, Long> idToFeatureMap = model.createMapIDFeaure();
		for (int i = 0; i < featureUser.size(); i++) {
			// get name feature
			String nameFeature = featureUser.get(i);
			// get id feature
			Long idFeature = idToFeatureMap.get(nameFeature);
//			try {
//				userVector.set(idFeature, userVector.get(idFeature) + 1);
//			} catch (IllegalArgumentException e) {
//				userVector.set(idFeature, 1);
//			}
			if (idFeature != null) {// feature item and user can not match beacause async
				userVector.set(idFeature, 1);
			}
		}
		// get number of features of user specific
		int numOfFeatureUserSpec = ScoreUtils.countFeatureSpecific(userVector);
		System.out.println("số "+numOfFeatureUserSpec);
		// compute TF-IDF in user vector
		long index = 1;
		for (VectorEntry ve : userVector) {
			double tf = ve.getValue() / numOfFeatureUserSpec;
			double df = model.weightDF(ve.getKey());
			double tf_idf = tf * Math.log(df);
//			System.out.println(tf_idf);
			userVector.set(index, tf_idf);
			index++;
		}

		return userVector;
	}
}
