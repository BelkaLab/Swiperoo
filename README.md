# Android-Toggle-Switch

![Alt text](https://img.shields.io/badge/license-MIT-green.svg?style=flat)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Android%20Toggle%20Switch-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/3235)
![Alt text](http://www.android-gems.com/badge/BelkaLab/Android-Toggle-Switch.svg)


A customizable extension of Android Switches that supports also more than 2 items.

![Sample of libray](docs/screen.jpg)


## Installation

#### Gradle
Add Gradle dependency:

```groovy
dependencies {
    compile 'us.belka:androidtoggleswitch:1.2.2'
}
```

#### Maven
```xml
<dependency>
  <groupId>us.belka</groupId>
  <artifactId>androidtoggleswitch</artifactId>
  <version>1.2.2</version>
  <type>pom</type>
</dependency>
```

## Usage

#### 2 Items 

```xml
<belka.us.androidtoggleswitch.widgets.ToggleSwitch
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        custom:textToggleLeft="OR"
        custom:textToggleRight="AND"/>
```

![Sample of libray with 2 items](docs/2_items.gif)

#### 3 Items

```xml
<belka.us.androidtoggleswitch.widgets.ToggleSwitch
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        custom:textToggleCenter="XOR"
        custom:textToggleLeft="OR"
        custom:textToggleRight="AND"/>
```

![Sample of libray with 3 items](docs/3_items.gif)

#### N - Items support

This can be accomplished only **programmatically**.

XML
```xml
<belka.us.androidtoggleswitch.widgets.ToggleSwitch
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"/>
```

JAVA code
```java
ToggleSwitch toggleSwitch = (ToggleSwitch) findViewById(R.id.multiple_switches);
ArrayList<String> labels = new ArrayList<>();
labels.add("AND");
labels.add("OR");
labels.add("XOR");
labels.add("NOT");
labels.add("OFF");
toggleSwitch.setLabels(labels);
```
![Sample of libray with 3 items](docs/n_items.gif)

NOTE: Providing a custom array of labels, the attributes textToggle[Left/Center/Right] will be ignored.

#### Multiple checked items support

```xml
<belka.us.androidtoggleswitch.widgets.MultipleToggleSwitch
        android:id="@+id/multiple_toggle_switch"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        custom:textToggleCenter="Center"
        custom:textToggleLeft="Left"
        custom:textToggleRight="Right"
        android:layout_gravity="center"
        custom:toggleWidth="82dp"/>                
```

NOTE: Please not that it's a **different** widget `MultipleToggleSwitch` instead of the previous `ToggleSwitch`.

## Getters and Setters


#### Toggle Switch

* `int getCheckedTogglePosition()` Returns the current checked position

```java
int position = toggleSwitch.getCheckedTogglePosition();
```

* `void setCheckedTogglePosition(int position)` Checks the position passed as argument

```java
int position = 3;
toggleSwitch.setCheckedTogglePosition(position);
```

#### Multiple Toggle Switch

* `void setCheckedTogglePosition(int position)` Checks the position passed as argument

```java
int position = 3;
multipleToggleSwitch.setCheckedTogglePosition(position);
```

* `void setUncheckedTogglePosition(int position)` Unchecks the position passed as argument

```java
int position = 0;
multipleToggleSwitch.setUncheckedTogglePosition(position);
```

* `Set<Integer> getCheckedTogglePositions()` Returns the set of the current checked positions

```java
Set<Integer> checkedPositions = multipleToggleSwitch.getCheckedTogglePositions();
```




## Listeners

```java
toggleSwitch.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener(){

            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
				// Write your code ... 
            }
        });
```


