package com.csjbot.blackgaga.cart.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.cart.entity.RobotMenuListBean;
import com.csjbot.blackgaga.util.RichTextUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.csjbot.blackgaga.model.http.product.ProductProxy.PRODUCT_IMG_PATH;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/16.
 */

public class ProductMainAdapter extends RecyclerView.Adapter<ProductMainAdapter.MyViewHolder> implements View.OnClickListener {
    public List<RobotMenuListBean.ResultBean.MenulistBean> loadData = new ArrayList<>();

    public void setImgItemClickListener(ImgItemClickListener imgItemClickListener) {
        this.imgItemClickListener = imgItemClickListener;
    }
    private ImgItemClickListener imgItemClickListener = null;
    private Context context;

    public ProductMainAdapter(Context context) {
        this.context = context;
    }

    public void updateRey(RobotMenuListBean data) {
        if (data != null) {
            if (data.getResult() == null) {
                loadData.clear();
                return;
            }
            RobotMenuListBean.ResultBean resultBean = data.getResult();
            if (resultBean.getMenulist() != null) {
                loadData = resultBean.getMenulist();
            } else {
                loadData.clear();
            }
        } else {
            loadData.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 获取到所有类别的名字
     */
    public SpannableStringBuilder getSortName() {
        RichTextUtil richTextUtil = new RichTextUtil();
        if (loadData != null) {
            int color = Color.parseColor("#0099ff");
            richTextUtil.append(context.getString(R.string.shop_products_is));
            for (int i = 0; i < loadData.size(); i++) {
                richTextUtil.append("\t" + (i + 1) + "." + loadData.get(i).getMenuName(), color);
            }
            richTextUtil.append("\t"+context.getString(R.string.say_need_product));
        }
        return richTextUtil.finish();
    }

    public List<RobotMenuListBean.ResultBean.MenulistBean> getLoadData() {
        return loadData;
    }

    @Override
    public ProductMainAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) {
            view = inflater.inflate(R.layout.vertical_product_item_main, parent, false);
        } else {
            view = inflater.inflate(R.layout.product_item_main, parent, false);
        }
        return new ProductMainAdapter.MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ProductMainAdapter.MyViewHolder holder, final int position) {
        String name = loadData.get(position).getMenuName();
        String imgName = loadData.get(position).getAliasName();
        String toFilePath = PRODUCT_IMG_PATH + imgName;
        holder.cateSort.setText(name);
        holder.itemView.setTag(position);
        if (new File(toFilePath).exists()) {
            Glide.with(context).load(toFilePath).into(holder.product_cate_img);
        } else {
            holder.product_cate_img.setImageResource(R.drawable.no_product_list);
        }
        holder.product_cate_img.setOnClickListener(v -> {
            if (imgItemClickListener != null) {
                imgItemClickListener.onClick(loadData.get(position));
            }
        });
    }


    @Override
    public int getItemCount() {
        return loadData.size();
    }


    @Override
    public void onClick(View v) {
//        if (mOnItemClickListener != null) {
//            //注意这里使用getTag方法获取position
//            mOnItemClickListener.onItemClick(v, (int) v.getTag());
//        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView cateSort;
        private ImageView product_cate_img;

        //控件
        public MyViewHolder(View itemView) {
            super(itemView);
            cateSort = itemView.findViewById(R.id.cate_sort);
            product_cate_img = itemView.findViewById(R.id.product_cate_img);
        }
    }

    public interface ImgItemClickListener {
        void onClick(RobotMenuListBean.ResultBean.MenulistBean menulistBean);
    }
}
