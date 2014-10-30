NLibs
=====

<h3>1. First, you must call init method of Nlibs class.</h3>
NLibs.init(this);

<h3>2. Using PhotoView object instead ImageView </h3>
< com.nlibs.imageloader.PhotoView <br>
        android:id="@+id/icon" <br>
        android:layout_width="100dp" <br>
        android:layout_height="100dp" />

<h3>3. In the subclass of BaseAdapter, using code below to display list image from URL</h3>
NLibs.getInstance(context).loadBitmap(photoview, new URL(strURL), R.drawable.no_image);
