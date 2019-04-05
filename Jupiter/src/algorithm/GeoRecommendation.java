package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;

// Recommendation based on geo distance and similar categories.
public class GeoRecommendation {
	public List<Item> recommendItems(String userId, double lat, double lon){
		List<Item> recommendedItems = new ArrayList<>();
		DBConnection conn = DBConnectionFactory.getConnection();
		
		try {
			// step 1: Get all favorite items
			Set<String>  favoriteItems = conn.getFavoriteItemIds(userId);
			
			// step 2: Get all categories of favorite items, sort by count
			Map<String, Integer> allCategories = new HashMap<>();
			for (String item : favoriteItems) {
				Set<String> categories = conn.getCategories(item);
				for (String category : categories) {
					allCategories.put(category, allCategories.getOrDefault(category, 0) + 1);
				}
			}
			List<Map.Entry<String, Integer>> categoryList = new ArrayList<>(allCategories.entrySet());
			Collections.sort(categoryList, (Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)->{
				return Integer.compare(o2.getValue(), o1.getValue());
			});
			
			// Step 3 do search based on category, filter out favorite events,
			// sort by distance
			Set<Item> visitedItems = new HashSet<>();
			for (Map.Entry<String, Integer> category : categoryList) {
				List<Item> items = conn.searchItems(lat, lon, category.getKey());
				List<Item> filteredItems = new ArrayList<>();
				
				for (Item item : items) {
					if (!favoriteItems.contains(item.getItemId())
							&& !visitedItems.contains(item)) {
						filteredItems.add(item);
					}
				}
				Collections.sort(filteredItems, (Item item1, Item item2)->{
					return Double.compare(item1.getDistance(), item2.getDistance());
				});
				visitedItems.addAll(items);
				recommendedItems.addAll(filteredItems);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return recommendedItems;
	}
}
