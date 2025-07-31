
# 소스 코드

```java
public class Main {  
    int radius;  
  
    public void setRadius(int r) {  
        radius = r;  
    }  
  
    public double calculateArea() {  
        return 3.14 * radius * radius;  
    }  
  
    public void printCircleInfo() {  
        System.out.println("반지름: " + radius);  
        System.out.println("넓이: " + calculateArea());  
    }  
  
    public static void main(String[] args)  
    {  
        Main main = new Main();  
        main.setRadius(5);  
        main.printCircleInfo();  
    }  
}
```

