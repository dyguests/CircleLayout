CircleLayout
============

To make child views be arranged in a circular.圆形布局.

使用方法:

    <com.fanhl.widget.CircleLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:endAngle="300"
        app:pathShape="circle"
        app:rotateDirection="counterclockwise"
        app:startAngle="60">

        <Button
            android:layout_width="wrap_content"
            android:layout_height="40dp"
            android:text="start" />

        <Button
            android:layout_width="40dp"
            android:layout_height="40dp"
            android:text="1" />

        <Button
            android:layout_width="40dp"
            android:layout_height="40dp"
            android:text="end" />
    </com.fanhl.widget.CircleLayout>

pathShape={circle,rectangle}
rotateDirection={clockwise,counterclockwise}
startAngle,endAngle=[0,360)

