package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import model.Goods;

public class ShoppingListTable {
	Connection conn = null;
	List<Goods> goodsList = new ArrayList<Goods>();

	public List<Goods> getAll(){
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/learning?useSSL=false","root","excite");

			String sql = "SELECT * FROM SHOPPING_LIST";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			ResultSet rs  = pStmt.executeQuery();


			//select文の結果を格納
			while(rs.next()) {
				String uuid = rs.getString("UUID");
				String name = rs.getString("ITEM");
				Integer number = rs.getInt("NUMBER");
				String memo = rs.getString("MEMO");
				String registeredDatetime = rs.getString("REGISTERED_DATETIME");
				String purchasedDatetime = rs.getString("PURCHASED_DATETIME");
				String updateDatetime = rs.getString("UPDATE_DATETIME");
				Goods goods = new Goods(uuid,name,number,memo,registeredDatetime,purchasedDatetime,updateDatetime);
				goodsList.add(goods);
			}
			//登録日が新しい順に並び替え
			class SortByDate implements Comparator<Goods>{
				@Override
				public int compare(Goods o1, Goods o2) {
					String date1 = o1.getRegisteredDatetime();
					String date2 = o2.getRegisteredDatetime();
					return date2.compareTo(date1);
				}
			}
			Collections.sort(goodsList,new SortByDate());

		} catch(SQLException e) {
				e.printStackTrace();
				return null;
		} catch(ClassNotFoundException e) {
				e.printStackTrace();
				return null;
		} finally {
			if(conn != null) {
				try {
					conn.close();
				}catch(SQLException e){
					e.printStackTrace();
					return null;
				}
			}
		}
			return goodsList;
		}


	public List<Goods> getAllNotYetParchased(){
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/learning?useSSL=false","root","excite");

			String sql = "SELECT * FROM SHOPPING_LIST";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			ResultSet rs  = pStmt.executeQuery();


			//select文の結果を格納
			while(rs.next()) {
				String uuid = rs.getString("UUID");
				String name = rs.getString("ITEM");
				Integer number = rs.getInt("NUMBER");
				String memo = rs.getString("MEMO");
				String registeredDatetime = rs.getString("REGISTERED_DATETIME");
				String purchasedDatetime = rs.getString("PURCHASED_DATETIME");
				//購入済みレコードは飛ばす
				if(purchasedDatetime != null) {
					continue;
				}
				String updateDatetime = rs.getString("UPDATE_DATETIME");
				Goods goods = new Goods(uuid,name,number,memo,registeredDatetime,purchasedDatetime,updateDatetime);
				goodsList.add(goods);
			}
			//登録日が新しい順に並び替え
			class SortByDate implements Comparator<Goods>{
				@Override
				public int compare(Goods o1, Goods o2) {
					String date1 = o1.getRegisteredDatetime();
					String date2 = o2.getRegisteredDatetime();
					return date2.compareTo(date1);
				}
			}
			Collections.sort(goodsList,new SortByDate());

		} catch(SQLException e) {
				e.printStackTrace();
				return null;
		} catch(ClassNotFoundException e) {
				e.printStackTrace();
				return null;
		} finally {
			if(conn != null) {
				try {
					conn.close();
				}catch(SQLException e){
					e.printStackTrace();
					return null;
				}
			}
		}
			return goodsList;
		}

	public Goods add(Goods goods) {
		Connection conn = null;
		try {
			//データベース接続
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/learning?useUnicode=true&characterEncoding=utf8","root","excite");

			String sql = "INSERT INTO SHOPPING_LIST(UUID,ITEM,NUMBER,MEMO,REGISTERED_DATETIME) VALUES(?,?,?,?,?)";
			PreparedStatement pStmt = conn.prepareStatement(sql);

			//UUIDを生成
			UUID uuid = UUID.randomUUID();
			//現在時刻を生成
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" );
			String currentTime = sdf.format(date);

			//?を設定
			pStmt.setString(1, uuid.toString());
			pStmt.setString(2, goods.getName());
			pStmt.setString(3, String.valueOf(goods.getNumber()));
			pStmt.setString(4, goods.getMemo());
			pStmt.setString(5, currentTime);

			//INSERT文を実行
			int result  = pStmt.executeUpdate();

		}catch(SQLException e) {
			e.printStackTrace();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}finally {
			//データベース切断
			if(conn != null) {
				try {
					conn.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return goods;
	}

	public void update(Goods goods) {
		Connection conn = null;
		try {
			//データベース接続
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/learning?useUnicode=true&characterEncoding=utf8","root","excite");
			String sql = "UPDATE SHOPPING_LIST SET ITEM = ? ,NUMBER = ? ,MEMO = ? ,UPDATE_DATETIME = ? where UUID = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);

			String purchaseSql = "UPDATE SHOPPING_LIST SET PURCHASED_DATETIME = ? where UUID = ?";
			PreparedStatement pStmtPurchase = conn.prepareStatement(purchaseSql);

			//現在時刻を生成
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" );
			String currentTime = sdf.format(date);

			//更新か購入済か判別
			if(goods.getPurchasedDatetime().equals("1")) {
				//購入済
				pStmtPurchase.setString(1, currentTime);
				pStmtPurchase.setString(2, goods.getUuid());
				int result  = pStmtPurchase.executeUpdate();
			}else {
				//更新
				pStmt.setString(1, goods.getName());
				pStmt.setString(2, String.valueOf(goods.getNumber()));
				pStmt.setString(3, goods.getMemo());
				pStmt.setString(4, currentTime);
				pStmt.setString(5, goods.getUuid());
				int result  = pStmt.executeUpdate();
			}

		}catch(SQLException e) {
			e.printStackTrace();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}finally {
			//データベース切断
			if(conn != null) {
				try {
					conn.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void delete(String uuid) {
		Connection conn = null;
		try {
			//データベース接続
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/learning?useUnicode=true&characterEncoding=utf8","root","excite");
			String sql = "DELETE FROM SHOPPING_LIST WHERE UUID = ? ";
			PreparedStatement pStmt = conn.prepareStatement(sql);

			//?を設定
			pStmt.setString(1,uuid);

			//INSERT文を実行
			int result  = pStmt.executeUpdate();

		}catch(SQLException e) {
			e.printStackTrace();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}finally {
			//データベース切断
			if(conn != null) {
				try {
					conn.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void purchase(String uuid) {
		Connection conn = null;
		try {
			//データベース接続
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/learning?useUnicode=true&characterEncoding=utf8","root","excite");
			String sql = "UPDATE SHOPPING_LIST SET PURCHASED_DATETIME = ? where UUID = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);

			//現在時刻を生成
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" );
			String currentTime = sdf.format(date);

			//?を設定
			pStmt.setString(1, currentTime);
			pStmt.setString(2, uuid);

			//INSERT文を実行
			int result  = pStmt.executeUpdate();

		}catch(SQLException e) {
			e.printStackTrace();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}finally {
			//データベース切断
			if(conn != null) {
				try {
					conn.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
