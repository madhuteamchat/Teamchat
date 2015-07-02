package com.teamchat.integrations.wunderlist;

import java.net.URLDecoder;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

public class SendResponseToUsers {
	public static String createEmbeddedLink(String url, String title,
			String protocol) {
		JSONObject object = new JSONObject();
		JSONObject web = new JSONObject();
		web.put("title", title);
		web.put("cancelBtnName", "Back");
		web.put("minWidth", "200");
		web.put("draggable", "true");
		web.put("newWindow", "true");
		web.put("url", url);
		object.put("web", web);
		byte[] byteArray = Base64.encodeBase64(object.toString().getBytes());
		String encodedString = new String(byteArray);
		String fUrl = protocol + "://teamchat:data=" + encodedString;

		return fUrl;

	}

	public void send(String jsonresp) {
		// TODO Auto-generated method stub
		JSONObject jobj = new JSONObject(jsonresp);
		JSONObject jclient = jobj.getJSONObject("client");
		String uid = null;
		new WebhookDB();
		if (jobj.getString("operation").equals("create")) {
			String print = "";
			JSONObject jsubj = jobj.getJSONObject("subject");
			String type = jsubj.getString("type");
			if (type.equals("task")) {
				uid = jclient.getString("user_id");
				JSONObject jdata = jobj.getJSONObject("data");
				String author_id = jdata.getInt("created_by_id") + "";
				String authorName = new GetDetails()
						.getUserName(uid, author_id);
				print = authorName + " created a ";

				if (jdata.getBoolean("starred")) {
					print = print + "Starred task named ";
				} else {
					print = print + "Task named ";
				}
				String name = jdata.getString("title");
				print = print + name + " in list ";
				JSONArray jparent = jsubj.getJSONArray("parents");
				int listid = jparent.getJSONObject(0).getInt("id");
				String listName = new GetDetails().getListName(uid, listid);
				print = print + listName;
				if (jdata.has("due_date")) {
					print = print + " with due date "
							+ jdata.getString("due_date");
				}
			} else if (type.equals("subtask")) {
				uid = jclient.getString("user_id");
				JSONObject jdata = jobj.getJSONObject("data");
				String author_id = jdata.getInt("created_by_id") + "";
				String authorName = new GetDetails()
						.getUserName(uid, author_id);
				int task_id = jdata.getInt("task_id");
				String subTaskName = jdata.getString("title");
				print = authorName + " created a subtask named " + subTaskName
						+ " in ";
				String[] taskName = new GetDetails().getTaskName(uid, task_id);
				print = print + "task " + taskName[0] + " in ";
				String listName = new GetDetails().getListName(uid,
						Integer.parseInt(taskName[1]));
				print = print + "list " + listName;

			} else if (type.equals("note")) {
				uid = jclient.getString("user_id");
				JSONObject jdata = jobj.getJSONObject("data");
				String content = jdata.getString("content");
				int task_id = jdata.getInt("task_id");
				print = "A new note created in ";
				String[] taskName = new GetDetails().getTaskName(uid, task_id);
				print = print + "task " + taskName[0] + " in ";
				String listName = new GetDetails().getListName(uid,
						Integer.parseInt(taskName[1]));
				print = print + "list " + listName;
				print = print + "<br>";
				print = print + "Content is : " + content;

			} else if (type.equals("task_comment")) {
				uid = jclient.getString("user_id");
				JSONObject jdata = jobj.getJSONObject("data");
				JSONObject jauthor = jdata.getJSONObject("author");
				String authorName = jauthor.getString("name");
				String text = jdata.getString("text");
				print = authorName + " made a comment to ";
				int task_id = jdata.getInt("task_id");
				String[] taskName = new GetDetails().getTaskName(uid, task_id);
				print = print + "task " + taskName[0] + " in ";
				String listName = new GetDetails().getListName(uid,
						Integer.parseInt(taskName[1]));
				print = print + "list " + listName;
				print = print + "<br>";
				print = print + "Comment is : " + text;
			} else if (type.equals("file")) {
				uid = jclient.getInt("user_id") + "";
				JSONObject jdata = jobj.getJSONObject("data");
				int userid = jdata.getInt("user_id");
				String author_id = jdata.getInt("created_by_id") + "";
				String authorName = new GetDetails().getUserName(userid + "",
						author_id);
				String fileName = jdata.getString("file_name");
				String fileType = jdata.getString("content_type");

				print = authorName + " attached a file to ";
				int task_id = jdata.getInt("task_id");
				String[] taskName = new GetDetails().getTaskName(userid + "",
						task_id);
				print = print + "task " + taskName[0] + " in list ";
				String listName = new GetDetails().getListName(uid,
						Integer.parseInt(taskName[1]));
				print = print + listName;
				print = print + "<br>";
				print = print + "File name : " + fileName + "<br>";
				print = print + "Type : " + fileType + "<br>";
				String url = URLDecoder.decode(jdata.getString("url"));
				String embedded_url=createEmbeddedLink(url, "download", "https");
				print = print + "<a href=" + embedded_url + ">Click to download</a>";
			}
			if (print.length() > 0) {
				String rid = WebhookDB.retrieveRoomId(uid);
				new WunderlistBot().postMsg(print, rid);
			}
		} else if (jobj.getString("operation").equals("delete")) {
			String print = "";
			JSONObject jsubj = jobj.getJSONObject("subject");
			String type = jsubj.getString("type");
			if (type.equals("file")) {
				String user_id = jobj.getJSONObject("client").getString(
						"user_id");
				String author_id = jobj.getJSONObject("after").getInt(
						"deleted_by_id")
						+ "";
				String author_name = new GetDetails().getUserName(user_id,
						author_id);
				print = print + author_name + " deleted a file named ";
				String file_name = jobj.getJSONObject("after").getString(
						"file_name");
				print = print + file_name + " present in task ";
				int task_id = jobj.getJSONObject("after").getInt("task_id");
				String[] taskName = new GetDetails().getTaskName(user_id,
						task_id);
				print = print + taskName[0] + " in list ";
				String listName = new GetDetails().getListName(user_id,
						Integer.parseInt(taskName[1]));
				print = print + listName + "<br>";
				String url = jobj.getJSONObject("after").getString("url");
				String decoded_url=URLDecoder.decode(url);
				String embedded_url = createEmbeddedLink(decoded_url, "download",
						"https");
				print = print + "<a href=" + embedded_url
						+ ">Click to Download</a>";

				if (print.length() > 0) {
					String rid = WebhookDB.retrieveRoomId(user_id);
					new WunderlistBot().postMsg(print, rid);
				}
			}
		} else if (jobj.getString("operation").equals("update")) {
			String print = "";
			String print1 = "";
			JSONObject jsubj = jobj.getJSONObject("subject");
			String type = jsubj.getString("type");
			if (type.equals("task")) {
				String user_id = jobj.getJSONObject("client").getString(
						"user_id");
				uid=user_id;
				String author_id = jobj.getJSONObject("after").getInt(
						"updated_by_id")
						+ "";
				String author_name = new GetDetails().getUserName(user_id,
						author_id);
				print = print + author_name + " updated a task named ";
				String task_name = jobj.getJSONObject("after").getString(
						"title");
				print = print + task_name + " in list ";
				int list_id = jobj.getJSONObject("after").getInt("list_id");
				String listName = new GetDetails()
						.getListName(user_id, list_id);
				print = print + listName + "<br>";
				if (!jobj
						.getJSONObject("after")
						.getString("title")
						.equals(jobj.getJSONObject("before").getString("title"))) {
					print1 = print1 + "Title is changed from "
							+ jobj.getJSONObject("before").getString("title")
							+ " to "
							+ jobj.getJSONObject("after").getString("title");
				}
				if (jobj.getJSONObject("before").getBoolean("completed")) {
					if (!jobj.getJSONObject("after").getBoolean("completed")) {
						print1 = print1 + "Task is incomplete<br>";
					}
				} else {
					if (jobj.getJSONObject("after").getBoolean("completed")) {
						print1 = print1 + "Task is completed<br>";
					}
				}
				if (jobj.getJSONObject("before").getBoolean("starred")) {
					if (!jobj.getJSONObject("after").getBoolean("completed")) {
						print1 = print1 + "Task is not starred<br>";
					}
				} else {
					if (jobj.getJSONObject("after").getBoolean("starred")) {
						print1 = print1 + "Task is starred<br>";
					}
				}
				if (jobj.getJSONObject("after").has("due_date")) {
					if (jobj.getJSONObject("before").has("due_date")) {
						String from = jobj.getJSONObject("before").getString(
								"due_date");
						String to = jobj.getJSONObject("after").getString(
								"due_date");
						print1 = print1 + "Due date changed from " + from
								+ " to " + to + "<br>";
					} else {
						String to = jobj.getJSONObject("after").getString(
								"due_date");
						print1 = print1 + "Due date set to " + to + "<br>";
					}
				} else if (jobj.getJSONObject("before").has("due_date")) {
					if (!jobj.getJSONObject("after").has("due_date")) {
						String due_date = jobj.getJSONObject("before")
								.getString("due_date");
						print1 = print1 + "Due date " + due_date
								+ " has been removed.<br>";
					}
				}
				if (print.length() > 0) {
					String rid = WebhookDB.retrieveRoomId(uid);
					new WunderlistBot().postMsg(print + print1, rid);
				}
			} else if (type.equals("subtask")) {
				uid = jobj.getJSONObject("client").getString("user_id");
				String author_id = jobj.getJSONObject("after").getInt(
						"updated_by_id")
						+ "";
				String authorName = new GetDetails()
						.getUserName(uid, author_id);
				int task_id = jobj.getJSONObject("after").getInt("task_id");
				String subTaskName = jobj.getJSONObject("after").getString(
						"title");
				print = authorName + " updated a subtask named " + subTaskName
						+ " in ";
				String[] taskName = new GetDetails().getTaskName(uid, task_id);
				print = print + "task " + taskName[0] + " in ";
				String listName = new GetDetails().getListName(uid,
						Integer.parseInt(taskName[1]));
				print = print + "list " + listName + "<br>";
				if (!jobj
						.getJSONObject("after")
						.getString("title")
						.equals(jobj.getJSONObject("before").getString("title"))) {
					print1 = print1 + "Title is changed from "
							+ jobj.getJSONObject("before").getString("title")
							+ " to "
							+ jobj.getJSONObject("before").getString("title");
				}
				if (jobj.getJSONObject("before").getBoolean("completed")) {
					if (!jobj.getJSONObject("after").getBoolean("completed")) {
						print1 = print1 + "Subtask is incomplete<br>";
					}
				} else {
					if (jobj.getJSONObject("after").getBoolean("completed")) {
						print1 = print1 + "Subtask is completed<br>";
					}
				}

				if (print.length() > 0) {
					String rid = WebhookDB.retrieveRoomId(uid);
					new WunderlistBot().postMsg(print + print1, rid);
				}
			} else if (type.equals("note")) {
				uid = jclient.getString("user_id");
				print = print + "Note present in task ";
				int task_id = jobj.getJSONObject("after").getInt("task_id");
				String[] taskName = new GetDetails().getTaskName(uid, task_id);
				print = print + taskName[0] + " in ";
				String listName = new GetDetails().getListName(uid,
						Integer.parseInt(taskName[1]));
				print = print + "list " + listName;
				print = print + "<br>";
				if (!jobj
						.getJSONObject("after")
						.getString("content")
						.equals(jobj.getJSONObject("before").getString(
								"content"))) {
					print1 = print1 + "Note content is changed to "
							+ jobj.getJSONObject("after").getString("content");
				}

				if (print.length() > 0) {
					String rid = WebhookDB.retrieveRoomId(uid);
					new WunderlistBot().postMsg(print + print1, rid);
				}
			}

		}
	}
}
