package picimage.utils.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.example.administrator.bigelephantbike.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ��SDCard�첽����ͼƬ
 *
 * @author hanj 2013-8-22 19:25:46
 */
public class SDCardImageLoader {
    //����
    private LruCache<String, Bitmap> imageCache;
    // �̶�2���߳���ִ������
    private ExecutorService executorService = Executors.newFixedThreadPool(2);
    private Handler handler = new Handler();

    private int screenW, screenH;

    public SDCardImageLoader(int screenW, int screenH) {
        this.screenW = screenW;
        this.screenH = screenH;

        // ��ȡӦ�ó����������ڴ�
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;

        // ����ͼƬ�����СΪ�����������ڴ��1/8
        imageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    private Bitmap loadDrawable(final int smallRate, final String filePath,
                                final ImageCallback callback) {
        // ���������ʹӻ�����ȡ������
        if (imageCache.get(filePath) != null) {
            return imageCache.get(filePath);
        }

        // �������û�����ȡSD��
        executorService.submit(new Runnable() {
            public void run() {
                try {
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(filePath, opt);

                    // ��ȡ�����ͼƬ��ԭʼ��Ⱥ͸߶�
                    int picWidth = opt.outWidth;
                    int picHeight = opt.outHeight;

                    //��ȡͼƬʧ��ʱֱ�ӷ���
                    if (picWidth == 0 || picHeight == 0) {
                        Log.d("num","fail");
                        return;
                    }

                    //��ʼѹ������
                    opt.inSampleSize = smallRate;
                    // �������Ĵ�С��ͼƬ��С��������ű���
                    if (picWidth > picHeight) {
                        if (picWidth > screenW)
                            opt.inSampleSize *= picWidth / screenW;
                    } else {
                        if (picHeight > screenH)
                            opt.inSampleSize *= picHeight / screenH;
                    }

                    //���������������һ�������صģ����������˵�bitmap
                    opt.inJustDecodeBounds = false;
                    final Bitmap bmp = BitmapFactory.decodeFile(filePath,opt);
                    //����map
                    imageCache.put(filePath, bmp);
                    handler.post(new Runnable() {
                        public void run() {
                            callback.imageLoaded(bmp);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return null;
    }

    /**
     * �첽��ȡSD��ͼƬ������ָ���ı�������ѹ������󲻳�����Ļ��������
     *
     * @param smallRate ѹ����������ѹ��ʱ����1����ʱ������Ļ�������������
     * @param filePath  ͼƬ��SD����ȫ·��
     * @param imageView ���
     */
    public void loadImage(int smallRate, final String filePath, final ImageView imageView) {

        Bitmap bmp = loadDrawable(smallRate, filePath, new ImageCallback() {

            @Override
            public void imageLoaded(Bitmap bmp) {
                if (imageView.getTag().equals(filePath)) {
                    if (bmp != null) {
                        imageView.setImageBitmap(bmp);
                    } else {
                        imageView.setImageResource(R.drawable.empty_photo);
                    }
                }
            }
        });

        if (bmp != null) {
            if (imageView.getTag().equals(filePath)) {
                imageView.setImageBitmap(bmp);
            }
        } else {
            imageView.setImageResource(R.drawable.empty_photo);
        }

    }


    // ����翪�ŵĻص��ӿ�
    public interface ImageCallback {
        // ע�� �˷�������������Ŀ������ͼ����Դ
        public void imageLoaded(Bitmap imageDrawable);
    }
}
