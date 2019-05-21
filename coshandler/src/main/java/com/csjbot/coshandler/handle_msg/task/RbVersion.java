package com.csjbot.coshandler.handle_msg.task;

import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.global.NTFConstants;
import com.csjbot.coshandler.global.RSPConstants;

/**
 * Created by jingwc on 2017/11/8.
 */

public class RbVersion extends RbBase {
    @Override
    protected void handleNTFMessage(String dataSource, String msgId) {
        switch (msgId){
            case NTFConstants.UPGRADE_PROGRESS_NTF:
                Robot.getInstance().pushUpgradeProgress(getIntSingleField(dataSource,"download_progress"));
                break;
            default:
                break;
        }
    }

    @Override
    protected void handleRSPMessage(String dataSource, String msgId) {
        switch (msgId){
            case RSPConstants.GET_VERSION_RSP:
                Robot.getInstance().pushVersion(getSingleField(dataSource,"version"));
                break;
            case RSPConstants.UPGRADE_CHECK_RSP:
                Robot.getInstance().pushUpgradeCheck(getIntSingleField(dataSource,"error_code"));
                break;
            case RSPConstants.UPGRADE_TOTAL_RSP:
                Robot.getInstance().pushUpgrade(getIntSingleField(dataSource,"error_code"));
                break;
            default:
                break;
        }
    }
}
