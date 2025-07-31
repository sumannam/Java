
# 구조

```
CircleCalculator/
├── src/
│   ├── Circle.java        # 원의 계산 로직을 담는 클래스
│   └── Main.java          # 프로그램 실행을 담당하는 메인 클래스
├── README.md              # 프로젝트 설명 파일
└── out/                   # 컴파일된 클래스 파일 저장 폴더
```

# src 0.5

## Circle.java

```java
public class Circle {
    // 필드(멤버 변수)
    private int radius;
    private String color;

    public double getArea() 
    {
	    return 3.14 * radius * radius;
	}
}
```

## Main.java

```java
public class Main {
	public static void main(String[] args)
	{
		Circle obj = new Circle();
		obj.radius = 100;
		obj.color = “blue”;
		double area = obj.getArea();
		System.out.println(area);
	}
}
```

---
# src v1.0

## Circle.jva


```java
public class Circle {  
    public int radius;  
    private String color;  
  
    public Circle()  
    {  
    }  
  
    public Circle(int radius, String color) {  
        this.radius = radius;  
        this.color = color;  
    }  
  
    public void setColor(String color)  
    {  
        this.color = color;  
    }  
  
    public double getArea()  
    {  
        return 3.14 * radius * radius;  
    }  
  
    public double getCircumFerence()  
    {  
        return 2 * 3.14 * radius;  
    }  
  
    public void printArea()  
    {  
        System.out.println(radius + " " + color);  
    }  
}
```


## Main.java


```java
public class Main {  
    public static void main(String[] args) {  
        Circle obj = new Circle();  
  
        obj.radius = 100;  
        obj.setColor("blue");  
  
        double area = obj.getArea();  
        System.out.println(area);  
    }  
}
```
