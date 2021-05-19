<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.Goods" %>
<%@ page import="java.util.List"%>

<%
//リクエストスコープに保存されたGoodsインスタンスを取得
List<Goods> goodsList = (List<Goods>)request.getAttribute("goodsList");
%>
<!DOCTYPE html>
	<html>
		<head>
			<meta charset="UTF-8">
			<title>買い物リスト画面</title>
			<link rel = "stylesheet" href = "style.css">
			<script type="text/javascript"></script>
			<script src="https://code.jquery.com/jquery-3.3.1.js"></script>
		</head>
		<body>
			<form name="form1" action="/shopping_list/ShoppingListServlet" method="post">
				<table class="tableRegister">
					<tr>
						<th><input type = "radio" class = "radioHidden"></th>
						<th class = "left">商品名</th>
						<th class = "left">個数</th>
						<th class = "left">メモ</th>
					</tr>
					<tr>
						<th><input type = "radio" class = "radioHidden"></th>
							<input type = "hidden" name = "hidUpdateUuid" id="hidUpdateUuid">
							<th class = "left"><input type = "text" name = "txtGoods" id = "txtGoods" class = "inputProduct"></th>
							<th class = "left"><input type = "text" name = "txtNumber" id="txtNumber" class = "inputQuantity"></th>
							<th class = "left"><input type = "text" name = "txtMemo" id="txtMemo" class = "inputNote"></th>
							<th><input type = "submit" name = "btnRegister" value="登録" class="btn"></th>
					</tr>
				</table>
				<p class="errorMsg">
				<% if(request.getAttribute("errorMsg")!=null){%>
					<%=request.getAttribute("errorMsg") %>
				<%}%>
				</p>
				<table class="tableList">
					<% for(int i = 0; i < goodsList.size(); i++){%>
					<tr>
						<th><input type = "radio" name="rdoSelect"></th>
						<input type = "hidden" name = "hidUuid" value="<%=goodsList.get(i).getUuid()%>">
						<th><input type = "text" class = "inputProduct" name = "lblGoods" value="<%= goodsList.get(i).getName() %>"></th>
						<th><input type = "text" class = "inputQuantity" name = "lblNumber" value="<%= goodsList.get(i).getNumber() %>"></th>
						<th><input type = "text" class = "inputNote" name = "lblMemo" value="<%= goodsList.get(i).getMemo()%>"></th>
						<th><input class="btn purchaseBtn" name="btnPurchase" type="submit" value="購入済" data-id ="<%= i%>"></th>
					</tr>
					<%} %>
				</table>
				<div class="bottombtn">
					<input type = "button" name = "btnUpdate" value="修正" class="btn center" onclick="radioCheck()">
					<input type = "submit" name = "btnDelete" id = "btnDelete" value="削除" class="btn center" onclick="setUuid()">
				</div>
			</form>

		<script>
		//ラジオボタン選択状態取得
 		function radioCheck(){

 			var rdoSelect = document.getElementsByName("rdoSelect");

			//ラジオボタンの数だけ判定を繰り返す
			for(var i = 0;i < rdoSelect.length;i++){
				if(rdoSelect[i].checked){

					var hidUpdateUuid = document.getElementsByName("hidUuid");
					var txtGoods = document.getElementsByName("lblGoods");
					var txtNumber = document.getElementsByName("lblNumber");
					var txtMemo = document.getElementsByName("lblMemo");

					document.getElementById("hidUpdateUuid").value = hidUpdateUuid[i].value;
					document.getElementById("txtGoods").value = txtGoods[i].value;
					document.getElementById("txtNumber").value = txtNumber[i].value;
					document.getElementById("txtMemo").value = txtMemo[i].value;

 				}
			}
		}

		//削除対象
		function setUuid(){
			var rdoSelect = document.getElementsByName("rdoSelect");
			for(var i = 0;i < rdoSelect.length;i++){
				if(rdoSelect[i].checked){
					var hidUpdateUuid = document.getElementsByName("hidUuid");
					document.getElementById("hidUpdateUuid").value = hidUpdateUuid[i].value;
 				}
			}
		}
		</script>
		<script>
			$('.purchaseBtn').on('click',function(){
				var id = $(this).data('id');
				var hidUpdateUuid = document.getElementsByName("hidUuid");
				document.getElementById("hidUpdateUuid").value = hidUpdateUuid[id].value;
			});
		</script>
	</body>
</html>