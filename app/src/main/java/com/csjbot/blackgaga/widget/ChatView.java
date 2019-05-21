package com.csjbot.blackgaga.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.home.ShuiWuHomeActivity3;
import com.csjbot.coshandler.log.CsjlogProxy;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天窗口View
 * Created by jingwc on 2017/10/16.
 */

public class ChatView extends FrameLayout {

    private Context mContext;

    private RecyclerView mRecyclerView;

    private List<CharSequence> mDatas;
    private Map<CharSequence, Integer> textmsgType = new IdentityHashMap<>();

    private ChatAdapter mAdapter;

    public ChatView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ChatView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChatView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化操作
     */
    public void init(Context context) {
        /* 初始化数据集合 */
        mDatas = new ArrayList<>();
        /* 获取当前上下文 */
        mContext = context;
        if (Constants.Scene.CurrentScene.equals(Constants.Scene.XingZheng)
                || Constants.Scene.CurrentScene.equals(Constants.Scene.CheGuanSuo)) {
            CsjlogProxy.getInstance().info("ChatView:XingZheng");
            if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                    || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) {
                LayoutInflater.from(mContext).inflate(R.layout.layout_chat_verticalscreen, this, true);
            } else {
                LayoutInflater.from(mContext).inflate(R.layout.layout_chat_admin, this, true);
            }
        } else if (Constants.Scene.CurrentScene.equals(Constants.Scene.ShuiWu)) {
            if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                    || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) {
                LayoutInflater.from(mContext).inflate(R.layout.layout_chat_verticalscreen, this, true);
            } else {
                if (context instanceof ShuiWuHomeActivity3) {
                    LayoutInflater.from(mContext).inflate(R.layout.layout_chat_admin, this, true);
                } else {
                    LayoutInflater.from(mContext).inflate(R.layout.layout_chat, this, true);
                }
            }
        } else {
            CsjlogProxy.getInstance().info("ChatView:");
            if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                    || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) {
            /* 加载布局文件到父View下 */
                LayoutInflater.from(mContext).inflate(R.layout.layout_chat_verticalscreen, this, true);
            } else {
                LayoutInflater.from(mContext).inflate(R.layout.layout_chat, this, true);
            }
        }

        /* RecyclerView初始化 */
        mRecyclerView = (RecyclerView) findViewById(R.id.chat_recycler);
        /* 设置布局管理器 */
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        /* 设置子项无间距 */
        mRecyclerView.addItemDecoration(new SpaceItemDecoration());
        /* 设置Adapter */
        mRecyclerView.setAdapter(mAdapter = new ChatAdapter());
    }


    public void removeBackgroud() {
        this.setBackgroundResource(0);
    }

    /**
     * 设置聊天内容
     *
     * @param msgType 消息类型(0:机器人,1:顾客)
     * @param text    聊天内容
     */
    public void addChatMsg(int msgType, CharSequence text) {
        /* 将内容和类型拼接成字符串添加到数据集合中 */
        this.mDatas.add(text);
        /* 将文字类型存入 MAP */
        this.textmsgType.put(text, msgType);
        /* 获取数据集合的最后一个item */
        int lastItemIndex = (mDatas.size() - 1);
        /* 刷新新增加的的item */
        this.mAdapter.notifyItemChanged(lastItemIndex);
        /* 滑动到最后一个item */
        this.mRecyclerView.smoothScrollToPosition(lastItemIndex);
    }

    /**
     * 清空聊天消息
     */
    public void clearChatMsg() {
        this.mDatas.clear();
        this.mAdapter.notifyDataSetChanged();
    }


    /**
     * 用来表示聊天内容的类型
     */
    public interface ChatMsgType {
        /**
         * 机器人聊天内容
         */
        int ROBOT_MSG = 0;
        /**
         * 顾客聊天内容
         */
        int CUSTOMER_MSG = 1;
    }

    private class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        ChatAdapter() {

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == ChatMsgType.ROBOT_MSG) {/* 机器人聊天内容 */
                // 加载机器人聊天内容布局
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_robot, parent, false);
                return new RobotViewHolder(view);
            } else if (viewType == ChatMsgType.CUSTOMER_MSG) {/* 顾客聊天内容 */
                // 加载顾客聊天内容布局
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_customer, parent, false);
                return new CustomerViewHodler(view);
            }

            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            /* 获取聊天内容类型 */
            int itemViewType = this.getItemViewType(position);

            if (itemViewType == ChatMsgType.ROBOT_MSG) {/* 机器人聊天内容类型 */
                // 将聊天内容设置到机器人聊天布局中
                ((RobotViewHolder) holder).setChatMsg(mDatas.get(position));
            } else if (itemViewType == ChatMsgType.CUSTOMER_MSG) {/* 顾客聊天内容类型 */
                // 将聊天内容设置到顾客聊天布局中
                ((CustomerViewHodler) holder).setChatMsg(mDatas.get(position));
            }
        }

        @Override
        public int getItemViewType(int position) {
            // 获取当前item的数据
            CharSequence value = mDatas.get(position);
            // 消息类型
            return textmsgType.get(value);
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class RobotViewHolder extends RecyclerView.ViewHolder {

            TextView tvMsg;

            RobotViewHolder(View itemView) {
                super(itemView);
                tvMsg = itemView.findViewById(R.id.tv_msg);
            }

            public void setChatMsg(CharSequence text) {
                if (!TextUtils.isEmpty(text)) {
                    tvMsg.setText(text);
                    tvMsg.setMovementMethod(LinkMovementMethod.getInstance());
                }
            }
        }

        class CustomerViewHodler extends RecyclerView.ViewHolder {

            TextView tvMsg;

            CustomerViewHodler(View itemView) {
                super(itemView);
                tvMsg = itemView.findViewById(R.id.tv_msg);
                tvMsg.setMovementMethod(LinkMovementMethod.getInstance());
            }

            public void setChatMsg(CharSequence text) {
                if (!TextUtils.isEmpty(text)) {
                    tvMsg.setText(text);
                }
            }

        }
    }

    /**
     * 调整recycler的间距
     */
    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        public SpaceItemDecoration() {

        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            /* 不设置间距 */
        }
    }
}
