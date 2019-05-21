package com.csjbot.blackgaga.feature.search;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.ai.SearchAI2;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.router.BRouterPath;
import com.csjbot.blackgaga.util.RichTextUtil;
import com.csjbot.blackgaga.widget.TitleView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiasuhuei321 on 2017/10/18.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

@Route(path = BRouterPath.INFO_SEARCH2)
public class SearchActivity2 extends BaseModuleActivity {


    SearchAI2 mAI;
    @BindView(R.id.tv_passenger_service)
    TextView mTvPassengerService;
    @BindView(R.id.tv_opening_hours)
    TextView mTvOpeningHours;
    @BindView(R.id.tv_service_notice)
    TextView mTvServiceNotice;
    @BindView(R.id.tv_the_card)
    TextView mTvTheCard;
    @BindView(R.id.title_view)
    TitleView mTitleView;
    @BindView(R.id.tv_textView)
    TextView mTvTextView;


    private TextView lastClick;

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        lastClick = mTvPassengerService;
        passengerservice();
    }

    @Override
    protected CharSequence initChineseSpeakText() {
        int color = Color.parseColor("#ffffff");
        SpannableStringBuilder ssb = new RichTextUtil().append("您好,请说出您要选择的查询类型\n")
                .append("\t1.旅客服务", color, v -> passengerservice())
                .append("\t2.开放时间", color, v -> openinghours())
                .append("\t3.服务须知", color, v -> servicenotice())
                .append("\t4.先关卡片", color, v -> thecard())

                .finish();
        return ssb;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }

    private void selectBgChange(TextView bt) {
        lastClick.setBackgroundResource(R.color.consultation_service_bg);
        lastClick.setTextColor(getResources().getColor(R.color.head_text_color));
        bt.setBackgroundResource(R.drawable.text_select_jichang);
        bt.setTextColor(getResources().getColor(R.color.head_text_color));
        lastClick = bt;
    }

    public void passengerservice() {
        selectBgChange(mTvPassengerService);
        mTvTextView.setText("\t发生航班延误后，航空公司、机场等民航保障单位会携手为旅客提供相应服务保障，尽快恢复航班运行正常。\n" +
                "1、信息告知\n" +
                "\n" +
                "航空公司会在第一时间将航班延误、取消、恢复信息通过候机楼内航班显示屏，企业官方网站、官方微博，电视、广播媒体等多重渠道实时滚动向旅客和社会发布，还同时会通过呼叫中心和短信平台向旅客实时发送航班调整信息，因此提醒旅客在购票时务必填写本人真实手机号码，以便及时获取航班调整信息。发生大面积航班延误时，机场将协助航空公司做好航班调整信息的对外发布工作。\n" +
                "2、服务保障\n" +
                "\n" +
                "航空公司会迅速补充地面服务保障人员做好旅客办理退改签手续，做好现场旅客的安抚和解释工作，同时会及时调配运力，做好航班补班计划，争取在最短的时间内恢复航班正常运行秩序。\n" +
                "\n" +
                "机场将确保航班延误期间候机楼内照明、通风、空调设施完好，确保候机楼内餐饮单位照常营业。在暴雨、冰雪天气过程中及时做好跑道、滑行道除冰雪和积水清理工作，保证航班正常运行。\n" +
                "3、旅客权利\n" +
                "\n" +
                "航班延误或取消，旅客可以选择改签或退票。不同的航空公司其退改签的规定会有所不同，请务必咨询航空公司。\n" +
                "\n" +
                "因航空公司原因造成航班在始发地延误或取消，在必要时航空公司应当向旅客提供免费膳宿服务等一定补偿。非航空公司原因造成航班在始发地延误或取消，航空公司应协助旅客安排膳宿，费用由旅客自理。 ");


    }

    public void openinghours() {
        selectBgChange(mTvOpeningHours);
        mTvTextView.setText("\t发生航班延误后，航空公司、机场等民航保障单位会携手为旅客提供相应服务保障，尽快恢复航班运行正常。\n" +
                "1、信息告知\n" +
                "\n" +
                "航空公司会在第一时间将航班延误、取消、恢复信息通过候机楼内航班显示屏，企业官方网站、官方微博，电视、广播媒体等多重渠道实时滚动向旅客和社会发布，还同时会通过呼叫中心和短信平台向旅客实时发送航班调整信息，因此提醒旅客在购票时务必填写本人真实手机号码，以便及时获取航班调整信息。发生大面积航班延误时，机场将协助航空公司做好航班调整信息的对外发布工作。\n" +
                "2、服务保障\n" +
                "\n" +
                "航空公司会迅速补充地面服务保障人员做好旅客办理退改签手续，做好现场旅客的安抚和解释工作，同时会及时调配运力，做好航班补班计划，争取在最短的时间内恢复航班正常运行秩序。\n" +
                "\n" +
                "机场将确保航班延误期间候机楼内照明、通风、空调设施完好，确保候机楼内餐饮单位照常营业。在暴雨、冰雪天气过程中及时做好跑道、滑行道除冰雪和积水清理工作，保证航班正常运行。\n" +
                "3、旅客权利\n" +
                "\n" +
                "航班延误或取消，旅客可以选择改签或退票。不同的航空公司其退改签的规定会有所不同，请务必咨询航空公司。\n" +
                "\n" +
                "因航空公司原因造成航班在始发地延误或取消，在必要时航空公司应当向旅客提供免费膳宿服务等一定补偿。非航空公司原因造成航班在始发地延误或取消，航空公司应协助旅客安排膳宿，费用由旅客自理。 ");
    }

    public void servicenotice() {
        selectBgChange(mTvServiceNotice);
        mTvTextView.setText("\t（一） 禁止进境物品\n" +

                "\n" +
                "３、珍贵文物及其它禁止出境的文物；\n" +
                "\n" +
                "４、濒危的和珍贵的动物植物（均含标本）及其种子和繁殖材料。\n" +
                "\n" +
                "（三）中华人民共和国限制进出境物品表　\n" +
                "\n" +
                "1、限制进境物品\n" +
                "\n" +
                "（1）无线电收发信机、通信保密机；\n" +
                "\n" +
                "（2）烟、酒；\n" +
                "\n" +
                "（3）濒危的和珍贵的动物、植物（均含标本）及其种子和繁殖材料；\n" +
                "\n" +
                "（4）国家货币；\n" +
                "\n" +
                "（5）海关限制进境的其它物品。\n" +
                "\n" +
                "2、限制出境物品\n" +
                "\n" +
                "（1）金银等贵重金属及其制品；\n" +
                "\n" +
                "（2）国家货币；\n" +
                "\n" +
                "（3）外币及其有价证券；\n" +
                "\n" +
                "（4）无线电收发信机、通信保密机；\n" +
                "\n" +
                "（5）贵重中药材；\n" +
                "\n" +
                "（6）一般文物；\n" +
                "\n" +
                "（7）、海关限制出境的其它物品。\n" +
                "\n" +
                "法律依据：海关总署令第43号\n");
    }

    public void thecard() {
        selectBgChange(mTvTheCard);
        mTvTextView.setText("\t《中华人民共和国出境入境管理法》\n" +
                "\n" +
                "第九条 中国公民出境入境，应当依法申请办理护照或者其他旅行证件。\n" +
                "\n" +
                "中国公民前往其他国家或者地区，还需要取得前往国签证或者其他入境许可证明。但是，中国政府与其他国家政府签订互免签证协议或者公安部、外交部另有规定的除外。\n" +
                "\n" +
                "中国公民以海员身份出境入境和在国外船舶上从事工作的，应当依法申请办理海员证。\n" +
                "\n" +
                "第十条 中国公民往来内地与香港特别行政区、澳门特别行政区，中国公民往来大陆与台湾地区，应当依法申请办理通行证件，并遵守本法有关规定。具体管理办法由国务院规定。\n" +
                "\n" +
                "第十一条 中国公民出境入境，应当向出入境边防检查机关交验本人的护照或者其他旅行证件等出境入境证件，履行规定的手续，经查验准许，方可出境入境。\n" +
                "\n" +
                "具备条件的口岸，出入境边防检查机关应当为中国公民出境入境提供专用通道等便利措施。\n" +
                "\n" +
                "第十二条 中国公民有下列情形之一的，不准出境：\n" +
                "\n" +
                "（一）未持有效出境入境证件或者拒绝、逃避接受边防检查的；\n" +
                "\n" +
                "（二）被判处刑罚尚未执行完毕或者属于刑事案件被告人、犯罪嫌疑人的；\n" +
                "\n" +
                "（三）有未了结的民事案件，人民法院决定不准出境的；\n" +
                "\n" +
                "（四）因妨害国（边）境管理受到刑事处罚或者因非法出境、非法居留、非法就业被其他国家或者地区遣返，未满不准出境规定年限的；\n" +
                "\n" +
                "（五）可能危害国家安全和利益，国务院有关主管部门决定不准出境的；\n" +
                "\n" +
                "（六）法律、行政法规规定不准出境的其他情形。");
    }

    @Override
    protected CharSequence initEnglishSpeakText() {
        return null;
    }



    @Override
    public void init() {
        super.init();
        mAI = SearchAI2.newInstance();
        mAI.initAI(this);

        getTitleView().setBackVisibility(View.VISIBLE);

    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override

    public int getLayoutId() {
        return R.layout.activity_search2;
    }
    @Override
    public boolean isOpenChat() {
        return true;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        SearchAI2.Intent intent = mAI.getIntent(text);
        if (intent != null) {
            mAI.handleIntent(intent);
        } else {
            prattle(answerText);
        }
        return true;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick({R.id.tv_passenger_service, R.id.tv_opening_hours, R.id.tv_service_notice, R.id.tv_the_card})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_passenger_service:
                passengerservice();
                break;
            case R.id.tv_opening_hours:
                openinghours();
                break;
            case R.id.tv_service_notice:
                servicenotice();
                break;
            case R.id.tv_the_card:
                thecard();
                break;
        }
    }


}
