package servlet;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ShoppingListTable;
import model.Goods;

/**
 * Servlet implementation class ShoppingListServlet
 */
@WebServlet("/ShoppingListServlet")
public class ShoppingListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShoppingListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		//全レコードを取得
		ShoppingListTable table = new ShoppingListTable();
		List<Goods> goodsList = table.getAllNotYetParchased();

		//リクエストスコープに保存
		request.setAttribute("goodsList", goodsList);
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/shopping_list.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		//登録ボタン
		String btnRegister = request.getParameter("btnRegister");
		//削除ボタン
		String btnDelete = request.getParameter("btnDelete");
		//購入済ボタン
		String btnPurchase = request.getParameter("btnPurchase");

		//リクエストパラメータを取得
		String updateUuid = request.getParameter("hidUpdateUuid");
		String name = request.getParameter("txtGoods");
		String txtnumber = request.getParameter("txtNumber");
		String memo = request.getParameter("txtMemo");

		if(btnRegister != null) {

			//個数が数値であるかチェック
			boolean flag = false;

			String reg = "^[1-9][0-9]*$";
			Pattern p = Pattern.compile(reg);
			Matcher m = p.matcher(txtnumber);
			if(m.find() == false && txtnumber.length()!=0) {
				flag = true;
			}

			if(txtnumber.length() > 3) {
				flag = true;
			}

			//Stringからintに変換
			Integer number = 0;

			if(txtnumber.length()!=0) {
				try {
					number = Integer.valueOf(txtnumber);
				}catch(NumberFormatException e){
					flag = true;
				}
			}

			//入力値妥当性チェック
			if(name.length()==0 || name.length() > 30) {
				String errorMsg = "登録する商品名が空であるか、最大入力文字数30文字を超えています。";
				request.setAttribute("errorMsg", errorMsg);
			}else if(flag) {
				String errorMsg = "登録する個数が数字以外であるか、最大入力桁数3桁を超えています。";
				request.setAttribute("errorMsg", errorMsg);
			}else if(memo.length()>200) {
				String errorMsg = "登録するメモの文字数が最大文字数200文字を超えています。";
				request.setAttribute("errorMsg", errorMsg);
			}else if(updateUuid.length()==0) {
				//新規登録
				registerGoods(name,number,memo);
			}else{
				//更新処理
				updateGoods(updateUuid,name,number,memo);
			}
		}
		//削除
		if(btnDelete != null) {
			deleteGoods(updateUuid);
		}

		//購入済み
		if("購入済".equals(btnPurchase)) {
			purchaseGoods(updateUuid);
		}

		//全レコードを取得
		ShoppingListTable table = new ShoppingListTable();
		List<Goods> goodsList = table.getAllNotYetParchased();

		//リクエストスコープに保存
		request.setAttribute("goodsList", goodsList);
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/shopping_list.jsp");
		dispatcher.forward(request, response);
	}

	private void registerGoods(String name,int number,String memo) {
		Goods goods = new Goods();
		goods.setName(name);
		goods.setNumber(number);
		goods.setMemo(memo);
		ShoppingListTable slTable = new ShoppingListTable();
		slTable.add(goods);
	}

	private void updateGoods(String uuid,String name,int number,String memo) {
		Goods goods = new Goods();
		goods.setUuid(uuid);
		goods.setName(name);
		goods.setNumber(number);
		goods.setMemo(memo);
		goods.setPurchasedDatetime("0");
		ShoppingListTable slTable = new ShoppingListTable();
		slTable.update(goods);
	}

	private void deleteGoods(String uuid) {
		ShoppingListTable slTable = new ShoppingListTable();
		slTable.delete(uuid);
	}

	private void purchaseGoods(String uuid) {
		Goods goods = new Goods();
		goods.setUuid(uuid);
		goods.setPurchasedDatetime("1");

		ShoppingListTable slTable = new ShoppingListTable();
		slTable.update(goods);
	}


}
