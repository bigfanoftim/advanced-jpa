# Projection

## 생성자를 이용한 Dto 조회
- Consturctor를 이용한 방식은 컴파일 단계에서 에러를 잡을 수 없다.
  ```java
    @Test
    public void findDtoByConstructor() throws Exception {
        UserDto result = query
                .select(Projections.constructor(
                        UserDto.class, 
                        user.name, 
                        user.age))
                .from(user)
                .where(user.age.eq(20))
                .fetchOne();

        assertThat(result.getName()).isEqualTo("A");
        assertThat(result.getAge()).isEqualTo(20);
    }
  ``` 
  위의 코드에서 생성자에 없는 `user.id` 필드가 추가되더라도 실제로 코드가 실행되는 시점까지 에러가 발생하지 않는다.

## QueryProjection
- Dto constructor에 `@QueryProjection` 어노테이션을 추가한다.
  ```java
    @QueryProjection
    public UserDto(String name, int age) {
        this.name = name;
        this.age = age;
    }
  ``` 
- querydsl compile을 진행하면 Dto에 대한 Q Class가 생성된다.
- Dto의 Q 객체를 사용함으로써 Dto 필드 타입에 꼭 맞게 타입 체킹을 할 수 있다.
  ```java
    @Test
    public void findDtoByQueryProjection() throws Exception {
        List<UserDto> fetch = query
                .select(new QUserDto(user.name, user.age))
                .from(user)
                .fetch();
    ...
    }
  ```
  
### QueryProjection의 단점

- `QueryProjection`을 이용하면 Repository뿐만 아니라 다양한 계층에서 사용하는 Dto가 Querydsl 라이브러리에 의존하는 아키텍쳐가 구성된다.
- 굉장히 실용적이긴 하지만 아키텍쳐를 보는 관점에 호불호가 갈릴 수 있다.
- 팀의 의견에 맞춰서 Projections, QueryProjection 중 하나를 잘 선택하자. 

### 개인적인 생각으로는...

- Dto가 Querydsl 라이브러리에 의존하게 되는 것은 맞지만 그렇다 하더라도 어노테이션 하나만 추가되는 것이기에 과연 큰 영향이 있을까? 하는 생각이 든다.
- `@QueryProjection` 어노테이션을 추가하더라도 기존의 Dto 동작에 영향을 주지 않는다면 `QDto 객체 생성을 위해 어노테이션 하나 추가했다` 정도로만 바라봐도 괜찮지 않을까?
  