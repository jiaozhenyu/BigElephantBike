package picimage.utils.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * ScreenUtils
 * Created by hanj on 14-9-25.
 */
public class ScreenUtils {
    private static int screenW;
    private static int screenH;
    private static float screenDensity;

    public static void initScreen(Activity mActivity){
        //DisplayMetrics ���ṩ��һ�ֹ�����ʾ��ͨ����Ϣ������ʾ��С���ֱ��ʺ����塣
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenW = metric.widthPixels;
        screenH = metric.heightPixels;
        screenDensity = metric.density;//�ܶ�
    }

    public static int getScreenW(){
        return screenW;
    }

    public static int getScreenH(){
        return screenH;
    }

    public static float getScreenDensity(){
        return screenDensity;
    }

    /** �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����) */
    public static int dp2px(float dpValue) {
        return (int) (dpValue * getScreenDensity() + 0.5f);
    }

    /** �����ֻ��ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp */
    public static int px2dp(float pxValue) {
        return (int) (pxValue / getScreenDensity() + 0.5f);
    }
}
