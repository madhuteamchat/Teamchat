package Task1a;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PollChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.client.sdk.*;

//import com.teamchat.client.sdk.chatlets.TextChatlet;

public class Leave_reporting {

    public static final String bot = "himanshu.rathee@st.niituniversity.in";
    public static final String password = "p@$$word4";
    public static final String requester = "rathee.coolhimanshu@gmail.com";
    public static final String manager = "user2@teamchat.com";
    public static final String HR = "user3@teamchat.com";
    

    @OnKeyword("leavereq")
    public void onLeavePost(TeamchatAPI api) {
        
        Room r1 = api.context().create()
                .add
                (manager)
                .add(HR);
        api.perform(r1.post(new PollChatlet().setQuestion(
                        "Allow my reuqest of leave").alias("manager")));
                        
    }

    @OnAlias("manager")
    public void onmanager(TeamchatAPI api) {
       String responsemanager = api.context().currentReply().getField("resp");
        
        if (responsemanager.equals("yes")){
            Room r2= api.context().create()
                    .add(manager)
                    .add(HR);
            api.perform(r2.post(new PollChatlet().setQuestion(
                    "Allow my reuqest of leave").alias("HR")));
        }
        else{
            api.perform(api.context().currentRoom().post(new TextChatlet("Application for leave REJECTED.")));
        }
        }

    @OnAlias("HR")
    public void onHR(TeamchatAPI api) {
       String responseHR = api.context().currentReply().getField("resp");
        
        if (responseHR.equals("yes")){
            api.perform(api.context().currentRoom().post(new TextChatlet("Application for leave ACCEPTED.")));
        }
        else{
            api.perform(api.context().currentRoom().post(new TextChatlet("Application for leave REJECTED.")));
        }
    }
    public static void main(String[] args) {
        TeamchatAPI api = TeamchatAPI.fromFile("teamchat5.data")
                .setEmail(bot).setPassword(password)
                .startReceivingEvents(new Leave_reporting());
    }

}
