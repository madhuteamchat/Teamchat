package com.teamchat.integrations.trello;

import org.json.JSONObject;

public class SendResponseToUsers {

	public void send(String response) {
		// TODO Auto-generated method stub
		String print = "";
		JSONObject jobj = new JSONObject(response);
		String user_id = jobj.getJSONObject("model").getString("id");
		// String user_name = jobj.getJSONObject("model").getString("fullName");
		String user_url = jobj.getJSONObject("model").getString("url");
		String embedd_url = TrelloBot.createEmbeddedLink(user_url, "Details",
				"https");
		String creator_id = jobj.getJSONObject("action")
				.getJSONObject("memberCreator").getString("id");
		String creator_name = new GetDetails().getMemberNamebyID(creator_id);

		if (jobj.getJSONObject("action").getString("type")
				.equals("createOrganization")) {
			print = print + "<a href=" + embedd_url + ">" + creator_name
					+ "</a>";
			String orzn_name = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("organization")
					.getString("name");
			print = print + " created a organization named " + orzn_name;

			String room_id = ManageDB.getRoomIDbyTrelloID(user_id);
			new TrelloBot().postMsg(print, room_id);

		} else if (jobj.getJSONObject("action").getString("type")
				.equals("addMemberToOrganization")) {
			print = print + "<a href=" + embedd_url + ">" + creator_name
					+ "</a>";
			String orzn_name = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("organization")
					.getString("name");
			String mem_id = jobj.getJSONObject("action").getJSONObject("data")
					.getString("idMemberAdded");
			String mem_name = new GetDetails().getMemberNamebyID(mem_id);
			print = print + " added a member named " + mem_name
					+ " to organization " + orzn_name;

			String room_id = ManageDB.getRoomIDbyTrelloID(user_id);
			new TrelloBot().postMsg(print, room_id);

		} else if (jobj.getJSONObject("action").getString("type")
				.equals("addToOrganizationBoard")) {
			print = print + "<a href=" + embedd_url + ">" + creator_name
					+ "</a>";
			String orzn_name = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("organization")
					.getString("name");
			String board_name = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("board")
					.getString("name");
			String board_link = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("board")
					.getString("shortLink");
			String board_url = "https://trello.com/b/" + board_link;
			String board_embed = TrelloBot.createEmbeddedLink(board_url,
					board_name, "https");
			print = print + " added a board named <a href=" + board_embed + ">"
					+ board_name + "</a> to organization " + orzn_name;
			String room_id = ManageDB.getRoomIDbyTrelloID(user_id);
			new TrelloBot().postMsg(print, room_id);

		} else if (jobj.getJSONObject("action").getString("type")
				.equals("createBoard")) {
			print = print + "<a href=" + embedd_url + ">" + creator_name
					+ "</a>";
			print = print + " created a new board named ";
			String board_name = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("board")
					.getString("name");
			String board_link = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("board")
					.getString("shortLink");
			String board_url = "https://trello.com/b/" + board_link;
			String board_embed = TrelloBot.createEmbeddedLink(board_url,
					board_name, "https");
			print = print + "<a href=" + board_embed + ">" + board_name
					+ "</a>";
			if (jobj.getJSONObject("action").getJSONObject("data")
					.has("organization")) {
				String orzn_name = jobj.getJSONObject("action")
						.getJSONObject("data").getJSONObject("organization")
						.getString("name");
				print = print + " in organization named " + orzn_name;
			}
			String room_id = ManageDB.getRoomIDbyTrelloID(user_id);
			new TrelloBot().postMsg(print, room_id);

		} else if (jobj.getJSONObject("action").getString("type")
				.equals("createCard")) {
			print = print + "<a href=" + embedd_url + ">" + creator_name
					+ "</a>";
			print = print + " created a new card named ";
			String card_name = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("card")
					.getString("name");
			String card_link = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("card")
					.getString("shortLink");
			String card_url = "https://trello.com/c/" + card_link;
			String card_embed = TrelloBot.createEmbeddedLink(card_url,
					card_name, "https");
			print = print + "<a href=" + card_embed + ">" + card_name + "</a>";
			String list_name = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("list")
					.getString("name");
			print = print + " in list named <b>" + list_name + "</b>";
			String board_name = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("board")
					.getString("name");
			String board_link = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("board")
					.getString("shortLink");
			String board_url = "https://trello.com/b/" + board_link;
			String board_embed = TrelloBot.createEmbeddedLink(board_url,
					board_name, "https");
			print = print + " in board named <a href=" + board_embed + ">"
					+ board_name + "</a>";
			if (jobj.getJSONObject("action").getJSONObject("data")
					.has("organization")) {
				String orzn_name = jobj.getJSONObject("action")
						.getJSONObject("data").getJSONObject("organization")
						.getString("name");
				print = print + " in organization named " + orzn_name;

			}
			String room_id = ManageDB.getRoomIDbyTrelloID(user_id);
			new TrelloBot().postMsg(print, room_id);

		} else if (jobj.getJSONObject("action").getString("type")
				.equals("createList")) {
			print = print + "<a href=" + embedd_url + ">" + creator_name
					+ "</a>";
			String list_name = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("list")
					.getString("name");
			print = print + " created a list named <b>" + list_name + "</b>";
			String board_name = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("board")
					.getString("name");
			String board_link = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("board")
					.getString("shortLink");
			String board_url = "https://trello.com/b/" + board_link;
			String board_embed = TrelloBot.createEmbeddedLink(board_url,
					board_name, "https");
			print = print + " in board named <a href=" + board_embed + ">"
					+ board_name + "</a>";
			if (jobj.getJSONObject("action").getJSONObject("data")
					.has("organization")) {
				String orzn_name = jobj.getJSONObject("action")
						.getJSONObject("data").getJSONObject("organization")
						.getString("name");
				print = print + " in organization named " + orzn_name;

			}
			String room_id = ManageDB.getRoomIDbyTrelloID(user_id);
			new TrelloBot().postMsg(print, room_id);

		} else if (jobj.getJSONObject("action").getString("type")
				.equals("addChecklistToCard")) {
			print = print + "<a href=" + embedd_url + ">" + creator_name
					+ "</a>";
			String cList_name = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("checklist")
					.getString("name");
			print = print + " created a checkList named " + cList_name;
			String card_name = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("card")
					.getString("name");
			String card_link = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("card")
					.getString("shortLink");
			String card_url = "https://trello.com/c/" + card_link;
			String card_embed = TrelloBot.createEmbeddedLink(card_url,
					card_name, "https");
			print = print + " in card named <a href=" + card_embed + ">"
					+ card_name + "</a>";
			String board_name = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("board")
					.getString("name");
			String board_link = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("board")
					.getString("shortLink");
			String board_url = "https://trello.com/b/" + board_link;
			String board_embed = TrelloBot.createEmbeddedLink(board_url,
					board_name, "https");
			print = print + " in board named <a href=" + board_embed + ">"
					+ board_name + "</a>";
			if (jobj.getJSONObject("action").getJSONObject("data")
					.has("organization")) {
				String orzn_name = jobj.getJSONObject("action")
						.getJSONObject("data").getJSONObject("organization")
						.getString("name");
				print = print + " in organization named " + orzn_name;

			}
			String room_id = ManageDB.getRoomIDbyTrelloID(user_id);
			new TrelloBot().postMsg(print, room_id);

		} else if (jobj.getJSONObject("action").getString("type")
				.equals("addAttachmentToCard")) {
			print = print + "<a href=" + embedd_url + ">" + creator_name
					+ "</a>";
			String attach_name = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("attachment")
					.getString("name");
			String attach_url = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("attachment")
					.getString("url");
			String attach_embed = TrelloBot.createEmbeddedLink(attach_url,
					attach_name, "https");
			print = print + " created an attachment named <a href="
					+ attach_embed + ">" + attach_name + "</a>";
			String card_name = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("card")
					.getString("name");
			String card_link = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("card")
					.getString("shortLink");
			String card_url = "https://trello.com/c/" + card_link;
			String card_embed = TrelloBot.createEmbeddedLink(card_url,
					card_name, "https");
			print = print + " in card named <a href=" + card_embed + ">"
					+ card_name + "</a>";
			String board_name = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("board")
					.getString("name");
			String board_link = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("board")
					.getString("shortLink");
			String board_url = "https://trello.com/b/" + board_link;
			String board_embed = TrelloBot.createEmbeddedLink(board_url,
					board_name, "https");
			print = print + " in board named <a href=" + board_embed + ">"
					+ board_name + "</a>";
			if (jobj.getJSONObject("action").getJSONObject("data")
					.has("organization")) {
				String orzn_name = jobj.getJSONObject("action")
						.getJSONObject("data").getJSONObject("organization")
						.getString("name");
				print = print + " in organization named " + orzn_name;
			}

			String room_id = ManageDB.getRoomIDbyTrelloID(user_id);
			new TrelloBot().postMsg(print, room_id);

		} else if (jobj.getJSONObject("action").getString("type")
				.equals("addMemberToCard")) {
			print = print + "<a href=" + embedd_url + ">" + creator_name
					+ "</a>";
			String mem_id = jobj.getJSONObject("action").getJSONObject("data")
					.getString("idMember");
			String mem_name = new GetDetails().getMemberNamebyID(mem_id);
			print = print + " added a member named " + mem_name;
			String card_name = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("card")
					.getString("name");
			String card_link = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("card")
					.getString("shortLink");
			String card_url = "https://trello.com/c/" + card_link;
			String card_embed = TrelloBot.createEmbeddedLink(card_url,
					card_name, "https");
			print = print + " in card named <a href=" + card_embed + ">"
					+ card_name + "</a>";
			String board_name = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("board")
					.getString("name");
			String board_link = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("board")
					.getString("shortLink");
			String board_url = "https://trello.com/b/" + board_link;
			String board_embed = TrelloBot.createEmbeddedLink(board_url,
					board_name, "https");
			print = print + " in board named <a href=" + board_embed + ">"
					+ board_name + "</a>";

			if (jobj.getJSONObject("action").getJSONObject("data")
					.has("organization")) {
				String orzn_name = jobj.getJSONObject("action")
						.getJSONObject("data").getJSONObject("organization")
						.getString("name");
				print = print + " in organization named " + orzn_name;
			}
			String room_id = ManageDB.getRoomIDbyTrelloID(user_id);
			new TrelloBot().postMsg(print, room_id);

		} else if (jobj.getJSONObject("action").getString("type")
				.equals("updateCard")) {
			print = print + "<a href=" + embedd_url + ">" + creator_name
					+ "</a>";
			System.out.println("check 1");
			if (jobj.getJSONObject("action").getJSONObject("data").has("old")) {
				JSONObject jold = jobj.getJSONObject("action")
						.getJSONObject("data").getJSONObject("old");
				System.out.println("check 2");
				if (jold.has("due")) {
					System.out.println("check 3");
					String due_date = jobj.getJSONObject("action")
							.getJSONObject("data").getJSONObject("card")
							.getString("due");
					System.out.println("check 4");
					print = print + " changed due date to " + due_date;
					String card_name = jobj.getJSONObject("action")
							.getJSONObject("data").getJSONObject("card")
							.getString("name");
					System.out.println("check 5");
					String card_link = jobj.getJSONObject("action")
							.getJSONObject("data").getJSONObject("card")
							.getString("shortLink");
					System.out.println("check 6");
					String card_url = "https://trello.com/c/" + card_link;
					String card_embed = TrelloBot.createEmbeddedLink(card_url,
							card_name, "https");
					print = print + " in card named <a href=" + card_embed
							+ ">" + card_name + "</a>";

				}
				if (jold.has("name")) {
					System.out.println("check 3");
					String old_name = jold.getString("name");
					System.out.println("check 4");
					print = print + " changed card name " + old_name + " to";
					String card_name = jobj.getJSONObject("action")
							.getJSONObject("data").getJSONObject("card")
							.getString("name");
					System.out.println("check 5");
					String card_link = jobj.getJSONObject("action")
							.getJSONObject("data").getJSONObject("card")
							.getString("shortLink");
					System.out.println("check 6");
					String card_url = "https://trello.com/c/" + card_link;
					String card_embed = TrelloBot.createEmbeddedLink(card_url,
							card_name, "https");
					print = print + " <a href=" + card_embed + ">" + card_name
							+ "</a>";

				}
			}
			String board_name = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("board")
					.getString("name");
			System.out.println("check 7");
			String board_link = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("board")
					.getString("shortLink");
			System.out.println("check 8");
			String board_url = "https://trello.com/b/" + board_link;
			String board_embed = TrelloBot.createEmbeddedLink(board_url,
					board_name, "https");
			print = print + " in board named <a href=" + board_embed + ">"
					+ board_name + "</a>";

			if (jobj.getJSONObject("action").getJSONObject("data")
					.has("organization")) {
				String orzn_name = jobj.getJSONObject("action")
						.getJSONObject("data").getJSONObject("organization")
						.getString("name");
				print = print + " in organization named " + orzn_name;
			}
			String room_id = ManageDB.getRoomIDbyTrelloID(user_id);
			new TrelloBot().postMsg(print, room_id);

		} else if (jobj.getJSONObject("action").getString("type")
				.equals("commentCard")) {
			print = print + "<a href=" + embedd_url + ">" + creator_name
					+ "</a>";
			String comment = jobj.getJSONObject("action").getJSONObject("data")
					.getString("text");
			print = print + " added a comment";
			String card_name = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("card")
					.getString("name");
			String card_link = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("card")
					.getString("shortLink");
			String card_url = "https://trello.com/c/" + card_link;
			String card_embed = TrelloBot.createEmbeddedLink(card_url,
					card_name, "https");
			print = print + " in card named <a href=" + card_embed + ">"
					+ card_name + "</a>";
			String board_name = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("board")
					.getString("name");
			String board_link = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("board")
					.getString("shortLink");
			String board_url = "https://trello.com/b/" + board_link;
			String board_embed = TrelloBot.createEmbeddedLink(board_url,
					board_name, "https");
			print = print + " in board named <a href=" + board_embed + ">"
					+ board_name + "</a>";

			if (jobj.getJSONObject("action").getJSONObject("data")
					.has("organization")) {
				String orzn_name = jobj.getJSONObject("action")
						.getJSONObject("data").getJSONObject("organization")
						.getString("name");
				print = print + " in organization named " + orzn_name;

			}
			print = print + "<br> Comment is:" + comment;
			String room_id = ManageDB.getRoomIDbyTrelloID(user_id);
			new TrelloBot().postMsg(print, room_id);

		} else if (jobj.getJSONObject("action").getString("type")
				.equals("updateComment")) {
			String new_comment = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("action")
					.getString("text");
			String old_comment = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("old")
					.getString("text");
			print = print + " modified a comment";
			String card_name = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("card")
					.getString("name");
			String card_link = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("card")
					.getString("shortLink");
			String card_url = "https://trello.com/c/" + card_link;
			String card_embed = TrelloBot.createEmbeddedLink(card_url,
					card_name, "https");
			print = print + " in card named <a href=" + card_embed + ">"
					+ card_name + "</a>";
			String board_name = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("board")
					.getString("name");
			String board_link = jobj.getJSONObject("action")
					.getJSONObject("data").getJSONObject("board")
					.getString("shortLink");
			String board_url = "https://trello.com/b/" + board_link;
			String board_embed = TrelloBot.createEmbeddedLink(board_url,
					board_name, "https");
			print = print + " in board named <a href=" + board_embed + ">"
					+ board_name + "</a>";

			if (jobj.getJSONObject("action").getJSONObject("data")
					.has("organization")) {
				String orzn_name = jobj.getJSONObject("action")
						.getJSONObject("data").getJSONObject("organization")
						.getString("name");
				print = print + " in organization named " + orzn_name;

			}
			print = print + "<br>Old comment is: " + old_comment;
			print = print + "<br>New comment is: " + new_comment;
			String room_id = ManageDB.getRoomIDbyTrelloID(user_id);
			new TrelloBot().postMsg(print, room_id);
		}
	}
}