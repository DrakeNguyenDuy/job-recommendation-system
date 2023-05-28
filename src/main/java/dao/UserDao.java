package dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.lenskit.data.dao.UserDAO;
import org.lenskit.data.entities.TypedName;

import anotations.UserFile;
import contants.AttributesContants;
import contants.IndexConstants;
import contants.SystemConstants;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongSet;

public class UserDao extends AbstractDao implements UserDAO {
	private final File userFile;
	private transient volatile Long2ObjectMap<List<String>> userFeatures;
//	private transient volatile LongSet userIds;

	@Inject
	public UserDao(@UserFile File file) {
		// TODO Auto-generated constructor stub
		this.userFile = file;
	}

	private void loadUser() {
		if (userFeatures == null) {
			synchronized (this) {
				if (userFeatures == null) {
					userFeatures = new Long2ObjectOpenHashMap<List<String>>();
					//type name of columns in file csv
					TypedName<?> typeNames[] = { AttributesContants.ENTITY_ID, AttributesContants.NAME,
							AttributesContants.SKILLS, AttributesContants.ADDRESS, AttributesContants.FEATURE4,
							AttributesContants.FEATURE5, AttributesContants.FEATURE6, AttributesContants.FEATURE7,
							AttributesContants.FEATURE8, AttributesContants.FEATURE9, AttributesContants.FEATURE10,
							AttributesContants.FEATURE11, AttributesContants.FEATURE12, AttributesContants.FEATURE13,
							AttributesContants.FEATURE14, AttributesContants.FEATURE15, AttributesContants.FEATURE16,
							AttributesContants.FEATURE17, AttributesContants.FEATURE18, AttributesContants.FEATURE19,
							AttributesContants.FEATURE20 };
					//columns need get value form file csv
					String[] columnsGet = { SystemConstants.ID, SystemConstants.NAME, SystemConstants.SKILLS,
							SystemConstants.ADDRESS };
					//All line in file csv
					List<String[]> lines = getObjectStream(typeNames, userFile, columnsGet);
					for (String[] line : lines) {
						//get id user
						long idUser = Long.parseLong(line[IndexConstants.I_ID]);
						//get list feature of item in userFeature map
						List<String> uf = userFeatures.get(idUser);
						if(uf==null) {
							uf = new ArrayList<String>();
							userFeatures.put(idUser, uf);
						}
						for (int i = 2; i < line.length; i++) {
							// begin is 2 beacause we will skip id and name
							// analys value in each column, value in each cell with format:
							// value1;value2;value3
							String[] analysh = line[i].split(SystemConstants.SEMICOMMA);
							for (String value : analysh) {
								// add value to features
								uf.add(value);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public LongSet getUserIds() {
		return userFeatures.keySet();
	}
	public List<String> getFeatureUser(long idUser){
		loadUser();
		return userFeatures.get(idUser);
	}
}
