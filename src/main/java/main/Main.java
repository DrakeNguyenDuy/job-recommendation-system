package main;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import org.grouplens.lenskit.scored.ScoredId;
import org.lenskit.LenskitConfiguration;
import org.lenskit.LenskitRecommender;
import org.lenskit.api.ItemRecommender;
import org.lenskit.api.ItemScorer;
import org.lenskit.api.Recommender;
import org.lenskit.data.dao.EventDAO;
import org.lenskit.data.dao.ItemDAO;
import org.lenskit.data.dao.UserDAO;

import anotations.ItemFile;
import anotations.RatingFile;
import anotations.UserFile;
import dao.ItemDao;
import dao.RatingDAO;
import dao.UserDao;

public class Main {
	public static void main(String[] args) {
		LenskitConfiguration config = configureRecommender();
		System.out.println("Starting build recommnder");
		Recommender rec = LenskitRecommender.build(config);
		// we automatically get a useful recommender since we have a scorer
		ItemRecommender irec = rec.getItemRecommender();
		String iu = "";
		long id;
		Scanner s = new Scanner(System.in);
		System.out.println("nhập gì đi");
		while (true) {
			iu = s.next();
			if (iu.equals("exit"))
				break;
			else {
				id = Long.parseLong(iu);
				System.out.println("searching for recommendations for user "+ id);
				List<Long> recs = irec.recommend(id, 5);
				if (recs.isEmpty()) {
					System.out.println("no recommendations for user "+ id);
				}
				System.out.format("recommendations for user %d:\n", id);
				for (Long i : recs) {
//					System.out.format("  %d: %.4f\n", i.getId(), i.getScore());
					System.out.println(i);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static LenskitConfiguration configureRecommender() {
		LenskitConfiguration config = new LenskitConfiguration();
		// use the TF-IDF scorer you will implement to score items
		config.bind(ItemScorer.class).to(TFIDFItemScorer.class);
		// configure the rating data source
		config.bind(EventDAO.class).to(RatingDAO.class);
		config.set(RatingFile.class).to(new File("data/1.csv"));
		// use custom item and user DAOs
		// specify item DAO implementation with features includes: skills, address,..
		config.bind(ItemDAO.class).to(ItemDao.class);
		// specify tag file
		config.set(ItemFile.class).to(new File("data/job.csv"));
		// our user DAO can look up by user name
		config.bind(UserDAO.class).to(UserDao.class);
		config.set(UserFile.class).to(new File("data/user.csv"));
		return config;
	}
}
