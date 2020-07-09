package com.example.articlemanagementsystem.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.articlemanagementsystem.Activity.UpdatePostActivity;
import com.example.articlemanagementsystem.Constant;
import com.example.articlemanagementsystem.Model.ArticleData;
import com.example.articlemanagementsystem.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.embersoft.expandabletextview.ExpandableTextView;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> {
    private Context context;
    private List<ArticleData> articleDataList;
    private Activity mActivity;
    private ArticleItemListener articleItemListener;



    public ArticleListAdapter(Context context, List<ArticleData> articleData, ArticleItemListener articleItemListener) {
        this.context = context;
       this.articleDataList = articleData;
       this.articleItemListener = articleItemListener;
    }
    private Context getContext() {
        return context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.layout_article_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem, articleItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ArticleData data = articleDataList.get(position);
        final String id = data.getId();
        holder.tv_articleTitle.setText(data.getArticleTitle());
        holder.tv_articleData.setText(data.getArticleData());
        holder.tv_articleDate.setText(data.getArticleDate());
        Picasso.get().load(data.getThumbnail()).resize(100, 100).into(holder.circularImageView_thumbnail);
        holder.tv_articleData.setOnStateChangeListener(new ExpandableTextView.OnStateChangeListener() {
            @Override
            public void onStateChange(boolean isShrink) {
                ArticleData articleData = articleDataList.get(position);
                articleData.setShrink(isShrink);
                articleDataList.set(position, articleData);
            }

        });
        holder.tv_articleData.setText(data.getArticleData());
        holder.tv_articleData.resetState(data.isShrink());



         holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //creating a popup menu

                Log.d("Tag", "Position " + holder.getAdapterPosition());
                 PopupMenu popup = new PopupMenu(context, holder.buttonViewOption);
                 try {
                     Method method = popup.getMenu().getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
                     method.setAccessible(true);
                     method.invoke(popup.getMenu(), true);
                     MenuInflater inflater = popup.getMenuInflater();
                     inflater.inflate(R.menu.opt_menu, popup.getMenu());
                     //inflating menu from xml resource
                     //adding click listener
                     popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                         @Override
                         public boolean onMenuItemClick(MenuItem item) {

                             switch (item.getItemId()) {
                                 case R.id.menu1:
                                     Intent intent = new Intent(context, UpdatePostActivity.class);
                                     intent.putExtra("ArticleID", id);
                                     getContext().startActivity(intent);
                                     ((Activity) getContext()).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                     //handle menu1 click
                                     //context.startActivity(new Intent(context, UpdatePostActivity.class));

                                     break;
                                 case R.id.menu2:
                                     //handle menu2 click
                                     AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                     alertDialogBuilder.setMessage("Are you sure to delete?");
                                     alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {
                                             deleteSelectedItem(id);
                                             articleItemListener.articleItemClick(holder.getAdapterPosition());
                                         }
                                     }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {

                                         }
                                     });
                                     alertDialogBuilder.create();
                                     alertDialogBuilder.show();

                                     break;

                             }
                             return false;
                         }
                     });
                     //displaying the popup
                     popup.show();
                 }catch (Exception e) {
                     e.printStackTrace();
                 }


             }
         });

    }

    @Override
    public int getItemCount() {
        return articleDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_articleTitle, tv_articleDate, buttonViewOption;
        ExpandableTextView tv_articleData;
        CircularImageView circularImageView_thumbnail;
        ArticleItemListener articleItemListener1;

        public ViewHolder(@NonNull View itemView, ArticleItemListener articleItemListener) {
            super(itemView);

            circularImageView_thumbnail = itemView.findViewById(R.id.image_thumbnail);
            tv_articleTitle = itemView.findViewById(R.id.txtView_articleTitle);
            tv_articleData = itemView.findViewById(R.id.txtView_articleContent);
            tv_articleDate = itemView.findViewById(R.id.txtView_articleDate_Posted);
            buttonViewOption = itemView.findViewById(R.id.txtView_Option);

            articleItemListener1 = articleItemListener;


        }
    }

    public interface  ArticleItemListener{
       void articleItemClick(int position);
    }

    public  void deleteSelectedItem(final String id){
        StringRequest request = new StringRequest(Request.Method.POST, Constant.API + "delete.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");

                    if(message.equals("success")){
                        Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);

                return params;
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);

    }


}
