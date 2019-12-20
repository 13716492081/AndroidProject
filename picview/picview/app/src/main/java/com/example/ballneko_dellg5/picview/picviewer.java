package com.example.ballneko_dellg5.picview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class picviewer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
//    private Integer[] images = {R.mipmap.nekos1,R.mipmap.nekos2,R.mipmap.nekos3,R.mipmap.neko};
    private Bitmap[] images=new Bitmap[101] ;
    private String file_path=Environment.getExternalStorageDirectory().getPath()+"/Tencent/狐狸/";
    public class ImagesChuan extends BaseAdapter {
        private Context mContext;
        public ImagesChuan(Context c){
            mContext = c;
        }
        public int getCount(){
            return images.length;
        }
        public Object getItem(int position) {
            return null;
        }
        public long getItemId(int a) {
            return 0;
        }
        //适配小图标
        public View getView(final int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if(convertView == null) {
                imageView = new ImageView(mContext);
//                imageView.setId(position);
                imageView.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(picviewer.this, ShowActivity.class);
                        Bundle bundle = new Bundle();
                        ByteArrayOutputStream out=new ByteArrayOutputStream();
                        images[position].compress(Bitmap.CompressFormat.PNG,100,out);
//                        Long("names",v.getId());

                        bundle.putByteArray("pic",out.toByteArray());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                });
                imageView.setLayoutParams(new GridView.LayoutParams(300,300));
                //裁剪居中
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            else {
                imageView = (ImageView) convertView;
            }
            //加载图片资源
//            imageView.setImageResource(images[position]);
            imageView.setImageBitmap(images[position] );
            return imageView;
        }
        public Context getmContext() { return mContext; }
        public void setmContext(Context mContext) {
            this.mContext = mContext;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picviewer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //调用系统原生拍照必要声明
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        if(ContextCompat.checkSelfPermission(picviewer.this, WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(picviewer.this,new String[]{WRITE_EXTERNAL_STORAGE},1);
        }else {
            load_pic();
        }

        final EditText editText =(EditText)findViewById(R.id.input);
        editText.setFocusable(false);
        Button search=(Button)findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputtext=editText.getText().toString();
                Toast.makeText(picviewer.this,inputtext,Toast.LENGTH_SHORT).show();
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        GridView gridview = (GridView)findViewById(R.id.gridView);
        gridview.setAdapter(new ImagesChuan(picviewer.this));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            //响应拍照结果
            case TAKE_PHOTO:
                    if(resultCode == RESULT_OK){
                        load_pic();
                    }
                break;
            default:
                break;

        }
    }
    private void load_pic(){
        System.out.println("173"+file_path);
        this.grantUriPermission("picviewr", Uri.parse(file_path), Intent.FLAG_GRANT_READ_URI_PERMISSION);
        File file_ = new File(file_path);
        File[] files=file_.listFiles();
        int count = files.length;
        for (int i=0;i<count;i++){
            File file = files[i];
            String filepath = file.getAbsolutePath();   //得到路径
            if (filepath.endsWith("jpg")||filepath.endsWith("png")){
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file_path+files[i].getName());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                BufferedInputStream bis = new BufferedInputStream(fis);
                Bitmap bitmap = BitmapFactory.decodeStream(bis);
//                //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//                ByteArrayOutputStream baos=new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                int options = 100;
//                //循环判断如果压缩后图片是否大于100kb,大于继续压缩
//                while (baos.toByteArray().length / 1024 > 100&&options>10) {
//                    //重置baos即清空baos
//                    baos.reset();
//                    //这里压缩options%，把压缩后的数据存放到baos中
//                    bitmap.compress(Bitmap.CompressFormat.PNG, options, baos);
//                    //每次都减少10
//                    options -= 10;
//                }
                images[i]=bitmap;
            }
            if(i>10){
                Log.i("已加载10张","");
                break;
            }
        }
    }
    public static final int TAKE_PHOTO = 1 ;
    private ImageView picture;
    private Uri imageUri;
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
//            //打开相机拍照
//            picture=(ImageView)findViewById(R.id.picture);
            File outputImage=new File(file_path,"output_image.jpg");
            try{
                if(outputImage.exists()){
                    outputImage.delete();
                }
                outputImage.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
//            if(Build.VERSION.SDK_INT>=24){
//                imageUri= FileProvider.getUriForFile(picviewer.this,"com.example.bnallneko_dellg5_picview.fileprovider",outputImage);
//            }
//            else{
//                imageUri=Uri.fromFile(outputImage);
//            }
            imageUri=Uri.fromFile(outputImage);
            Intent intent =new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
            startActivityForResult(intent,TAKE_PHOTO);
        }
        else if (id == R.id.nav_share) {
//            //分享到指定应用
//            Toast.makeText(picviewer.this,"已复制链接到剪贴板",Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            load_pic();
        }else {
            Toast.makeText(this, "权限不足!将无法读写本地文件!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}
