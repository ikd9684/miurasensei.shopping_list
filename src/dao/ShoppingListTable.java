package dao;
// FIXME パッケージ名は、例えば三浦君なら以下のようにして、世界中の誰とも競合しないようにすると良いです。
// FIXME 例：jp.co.excite_software.toshihiko_miura.study.shopping_list.dao

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

    // FIXME JavaDocコメントを書きましょう
    Connection conn = null; // FIXME privateにしなくとも良いですか？
    List<Goods> goodsList = new ArrayList<Goods>(); // FIXME privateにしなくとも良いですか？

    // FIXME 以下のgetAll()メソッドを使用しているところで、getAll()メソッドにカーソルを合わせてみてください。
    /**
     * 品物の全件を登録日が新しい順に並び替えて返します。
     * @return 品物の全件（登録日が新しい順）
     */
    public List<Goods> getAll() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // FIXME この記述 Class.forName は古いやりかたです、無くとも動かないですかね？
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/learning?useSSL=false", "root", "excite");
            // FIXME 同じ記述がメソッド単位に複数あります。
            // FIXME 「DRY原則」を意識してください。
            // FIXME また、文字列リテラルは誤記があっても動かすまでわかりません。場合によっては動かしても
            // FIXME 明確に例外が発生したりせず、ちょっと変な動きをする、もしくは見た目上正しそうに動く、
            // FIXME といった自体になることがあります。
            // FIXME その文字列リテラルを、同じことを、あちこちに書いておくと、修正したときに漏れたり、間違ったり、
            // FIXME するリスクが高まるだけです。
            // FIXME その意味でも、この記述はメソッドにして１個所にまとめるのが望ましいです。

            String sql = "SELECT * FROM SHOPPING_LIST";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            ResultSet rs = pStmt.executeQuery();
            // FIXME ConnectionだけでなくPreparedStatementもResultSetもcloseしてください。
            // FIXME PreparedStatementもResultSetも AutoCloseable なので、
            // FIXME リソース付きトライブロックの書き方ができるはずで、そうすると close の記述を省略できます。

            // select文の結果を格納
            while (rs.next()) {
                String uuid = rs.getString("UUID");
                String name = rs.getString("ITEM");
                Integer number = rs.getInt("NUMBER");
                String memo = rs.getString("MEMO");
                String registeredDatetime = rs.getString("REGISTERED_DATETIME");
                String purchasedDatetime = rs.getString("PURCHASED_DATETIME");
                String updateDatetime = rs.getString("UPDATE_DATETIME");
                Goods goods = new Goods(uuid, name, number, memo, registeredDatetime, purchasedDatetime,
                        updateDatetime);
                goodsList.add(goods);
            }
            // 登録日が新しい順に並び替え
            class SortByDate implements Comparator<Goods> {
                // FIXME このクラス定義はメソッドの外に出してください。
                // FIXME getAllNotYetParchased の中でも全く同じものを使っていますよね？
                // FIXME 「DRY原則」を意識してください。

                @Override
                public int compare(Goods o1, Goods o2) {

                    String date1 = o1.getRegisteredDatetime();
                    String date2 = o2.getRegisteredDatetime();
                    return date2.compareTo(date1);
                }
            }
            Collections.sort(goodsList, new SortByDate());

        }
        catch (SQLException e) {
            // FIXME catch ブロックの中の処理が例外の種類によらず同じで良いなら、
            // FIXME catch (SQLException | ClassNotFoundException e) { こう書けます。
            e.printStackTrace();
            // FIXME return Collections.emptyList(); とすると、
            // FIXME 受け取る側が null を意識する必要がなくなって安全です。
            return null;
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        finally { // FIXME リソース付きトライブロックを使うと finally ブロックの内容が不要になります。
            if (conn != null) {
                try {
                    conn.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        // FIXME 以下の一文は try ブロック内に書いてください。理由は、調べてみてください。
        return goodsList;
    }

    public List<Goods> getAllNotYetParchased() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/learning?useSSL=false", "root", "excite");

            String sql = "SELECT * FROM SHOPPING_LIST";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            ResultSet rs = pStmt.executeQuery();

            // select文の結果を格納
            while (rs.next()) {
                String uuid = rs.getString("UUID");
                String name = rs.getString("ITEM");
                Integer number = rs.getInt("NUMBER");
                String memo = rs.getString("MEMO");
                String registeredDatetime = rs.getString("REGISTERED_DATETIME");
                String purchasedDatetime = rs.getString("PURCHASED_DATETIME");
                // 購入済みレコードは飛ばす
                if (purchasedDatetime != null) {
                    continue;
                }
                String updateDatetime = rs.getString("UPDATE_DATETIME");
                Goods goods = new Goods(uuid, name, number, memo, registeredDatetime, purchasedDatetime,
                        updateDatetime);
                goodsList.add(goods);
            }
            // 登録日が新しい順に並び替え
            class SortByDate implements Comparator<Goods> {

                @Override
                public int compare(Goods o1, Goods o2) {

                    String date1 = o1.getRegisteredDatetime();
                    String date2 = o2.getRegisteredDatetime();
                    return date2.compareTo(date1);
                }
            }
            Collections.sort(goodsList, new SortByDate());

        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                }
                catch (SQLException e) {
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
            // データベース接続
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/learning?useUnicode=true&characterEncoding=utf8", "root", "excite");

            String sql = "INSERT INTO SHOPPING_LIST(UUID,ITEM,NUMBER,MEMO,REGISTERED_DATETIME) VALUES(?,?,?,?,?)";
            PreparedStatement pStmt = conn.prepareStatement(sql);

            // UUIDを生成
            UUID uuid = UUID.randomUUID();
            // 現在時刻を生成
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String currentTime = sdf.format(date);

            // ?を設定
            pStmt.setString(1, uuid.toString());
            pStmt.setString(2, goods.getName());
            // FIXME pStmt.setInt(3, goods.getNumber()); こうでは？
            pStmt.setString(3, String.valueOf(goods.getNumber()));
            pStmt.setString(4, goods.getMemo());
            // FIXME 以下は「暗黙の型変換」を利用していますね、意識していますか？
            // FIXME 理解して使っているのであれば良いですが、そうれなければ一度調べてみてください。
            pStmt.setString(5, currentTime);

            // INSERT文を実行
            int result = pStmt.executeUpdate();

            // FIXME goods.setUuid(uuid.toString()); を入れることで、
            // FIXME 呼び出し元が insert された結果を update とかにそのまま使用することが可能になります。

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            // データベース切断
            if (conn != null) {
                try {
                    conn.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return goods; // FIXME 例外があっても何事も無かったようにこれを返して良いですか？
    }

    public void update(Goods goods) {

        Connection conn = null;
        try {
            // データベース接続
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/learning?useUnicode=true&characterEncoding=utf8", "root", "excite");
            String sql = "UPDATE SHOPPING_LIST SET ITEM = ? ,NUMBER = ? ,MEMO = ? ,UPDATE_DATETIME = ? where UUID = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);

            String purchaseSql = "UPDATE SHOPPING_LIST SET PURCHASED_DATETIME = ? where UUID = ?";
            PreparedStatement pStmtPurchase = conn.prepareStatement(purchaseSql);

            // 現在時刻を生成
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String currentTime = sdf.format(date);

            // 更新か購入済か判別
            if (goods.getPurchasedDatetime().equals("1")) {
                // 購入済
                pStmtPurchase.setString(1, currentTime);
                pStmtPurchase.setString(2, goods.getUuid());
                int result = pStmtPurchase.executeUpdate();
            }
            else {
                // 更新
                pStmt.setString(1, goods.getName());
                pStmt.setString(2, String.valueOf(goods.getNumber()));
                pStmt.setString(3, goods.getMemo());
                pStmt.setString(4, currentTime);
                pStmt.setString(5, goods.getUuid());
                int result = pStmt.executeUpdate();
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            // データベース切断
            if (conn != null) {
                try {
                    conn.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void delete(String uuid) {

        Connection conn = null;
        try {
            // データベース接続
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/learning?useUnicode=true&characterEncoding=utf8", "root", "excite");
            String sql = "DELETE FROM SHOPPING_LIST WHERE UUID = ? ";
            PreparedStatement pStmt = conn.prepareStatement(sql);

            // ?を設定
            pStmt.setString(1, uuid);

            // INSERT文を実行
            int result = pStmt.executeUpdate();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            // データベース切断
            if (conn != null) {
                try {
                    conn.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void purchase(String uuid) {

        Connection conn = null;
        try {
            // データベース接続
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/learning?useUnicode=true&characterEncoding=utf8", "root", "excite");
            String sql = "UPDATE SHOPPING_LIST SET PURCHASED_DATETIME = ? where UUID = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);

            // 現在時刻を生成
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String currentTime = sdf.format(date);

            // ?を設定
            pStmt.setString(1, currentTime);
            pStmt.setString(2, uuid);

            // INSERT文を実行
            int result = pStmt.executeUpdate();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            // データベース切断
            if (conn != null) {
                try {
                    conn.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
