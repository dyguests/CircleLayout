# Android CircleLayout

To make child views be arranged in a circular.圆形布局.

![circle](/graphics/circle.png)
![rect](/graphics/rect.png)
![custom](/graphics/custom.png)

# Installlation


Copy CircleLayout.java to your project.

Copy \<declare-styleable name="CircleLayout"\> in attrs.xml to your attrs.xml.

# Usage

Parameters:

startAngle,endAngle should be a int from 0 to 360,the default value is 0.

    startAngle,endAngle=[0,360)

pathShape should be circle or rectangle,the default value is circle.

    pathShape={circle,rectangle}

rotateDirection should be clockwise or counterclockwise,the default vlaue is clockwise.

    rotateDirection={clockwise,counterclockwise}

# Sample

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
            android:layout_width="wrap_content"
            android:layout_height="40dp"
            android:text="end" />
    </com.fanhl.widget.CircleLayout>

# License

This project is licensed under the MIT License.Please refer the [License.txt](/License.txt) file.
Just use it. LOL.