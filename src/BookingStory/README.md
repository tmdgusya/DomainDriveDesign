# 선박 화물의 운송 예약 시스템

## 예약 어플리케이션 요구사항

- 각 **Cargo(화물)** 을 하나의 **Voyage(운항)** 와 연관관계를 맺고, 그걸 기록한다.
- 항상 언제든지 예약은 취소 될 수 있으므로, 선박이 운항중에 나를 수 있는 최대치보다 예약을 더 받는 것이 해운산업의 관행이다.

## 설계

- 위의 요구사항을 보고, Voyage 의 모델을 잡아보자

### Voyage

- **Voyage(운항)** 는 **Cargo(화물)** 와 **1:N 의 관계**를 지닌다.
- 요구사항에서는 현재 운항은 **자신만의 수용량**을 가지고 있다.
- Cargo 를 Confirmation 정보를 통해서 저장한다.

|Voyage|
|---|
|capacity|

### Cargo

- 요구사항에서 현재 화물은 **크기** 를 가질 수 있다.

|Cargo|
|---|
|size|

### ReservationService

- 예약 서비스는 화물을 운항과 연관관계를 맺고, 해당 내용을 **기록-관리** 하는 것이다.
- **confirmation** 은 주문 순서를 나타낸다.

|Reservation|
|---|
|confirmation|

## 코드 설계

### Voyage

- Voyage 는 모델 요구사항에 맞게 Confirmation(Integer) 를 통해서 Cargo 를 저장한다.
- 따라서 **addCargo** 라는 **메소드를 통해서** 자신의 **containers** 에 **Cargo** 를 저장한다.

```java
import java.util.HashMap;
import java.util.Map;

public class Voyage {

    private static final int MAX_CONTAIN_SIZE = 10;

    private final Map<Integer, Cargo> containers = new HashMap<>();

    public void addCargo(int confirmation, Cargo cargo) {
        containers.put(confirmation, cargo);
    }

    public int currentRemainContainerSize() {
        return MAX_CONTAIN_SIZE - containers.size();
    }

}
```

### Cargo

- Cargo 는 모델 요구사항에 맞게 자신이 담을 수 있는 물건의 Size 를 갖는다.

```java
public class Cargo {

    private final int size;

    public Cargo(int size) {
        this.size = size;
    }

    public int size() {
        return size;
    }

}

```

### ReservationService

- ReservationService 는 confirmation 과 Voyage 를 통해서 예약을 만들어야 한다.

```java
public class ReservationService {

    public int makeBooking(Cargo cargo, Voyage voyage) {
        int confirmation = 1;
        voyage.addCargo(confirmation, cargo);
        return confirmation;
    }

}
```

## 추가 요구사항

- 만약 예약에 정책적으로 OverBooking 을 한다고 해보자

- 아까 말했듯이 OverBooking 이란 해운사의 관례처럼 수용량의 110% 를 더 받는 것을 뜯한다.
  
```java
public int makeBooking(Cargo cargo, Voyage voyage) {
    double overBooking = voyage.MAX_CONTAIN_SIZE * 1.1;
    if ((voyage.currentRemainContainerSize() + Cargo.size()) > overBooking ) {
        return -1;    
    }
    int confirmation = 1;
    voyage.addCargo(confirmation, cargo);
    return confirmation;
}
```

- 위의 코드를 보자 코드 상으로는 이상할게 없어보인다.
- 근데 ... 만약 우리가 예약 도메인의 전문가와 얘기한다면, 해당 전문가가 이 코드를 잘 이해할 수 있을까?? 잘 볼 수 있냐는 뜻이다.
- 그래서 우리는 이러한 정책적인 부분을 BookingPolicy 라는 곳으로 떼어내보는 작업을 해야한다.

### OverBookingPolicy

```java
public class ReservationService {

    OverBookingPolicy overBookingPolicy = new OverBookingPolicy();

    public int makeBooking(Cargo cargo, Voyage voyage) {
        if(!overBookingPolicy.isAllowed(cargo, voyage)) {
            return -1;
        }
        int confirmation = 1;
        voyage.addCargo(confirmation, cargo);
        return confirmation;
    }

}
```

- 우리는 OverBookingPolicy 라는 것을 분리해냈다. 이제 해당 도메인에 관련된 사람이 보더라도, overBooking 정책이 승인(allow)
되어야만 밑으로 간다는 것을 쉽게 알 수 있을 것이다.
  
당신이 개발자임에도 불구하고, 현재 코드를 보고 해운시스템을 이해할 수 있다면, 잘 짜여진 코드임이 분명하다.