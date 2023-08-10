# DynamicQuery

## BooleanBuilder 활용

```java
    @Test
    public void dynamicQueryBooleanBuilder() throws Exception {
        String name = "A";
        Integer age = 20;

        List<User> result = searchMember1(name, age);

        assertThat(result.size()).isEqualTo(1);
    }

    private List<User> searchMember1(String nameCond, Integer ageCond) {
        /**
         * Builder 생성자로 초기값을 넘겨도 된다.
         * @example BooleanBuilder builder = new BooleanBuilder(user.name.eq(userCond));
         */
        BooleanBuilder builder = new BooleanBuilder();

        if (nameCond != null) {
            builder.and(user.name.eq(nameCond));
        }

        if (ageCond != null) {
            builder.and(user.age.eq(ageCond));
        }

        return query
                .select(user)
                .from(user)
                .where(builder)
                .fetch();
    }
```

- 왠지 Django의 Q 객체를 다루는 느낌이 강하다. 
- 내가 생각했을 때는 이렇게 조건을 절차적으로 나열하게 되면 파라미터가 많아질 경우 모든 조건을 전부 파악해야만 해당 쿼리의 역할을 정확히 이해할 수 있다는 문제가 있다.

## Where절에 들어갈 조건을 메소드로 분리

```java
    @Test
    public void dynamicQueryWhereParam() throws Exception {
        String name = "A";
        Integer age = 20;

        List<User> result = searchMember2(name, age);

        assertThat(result.size()).isEqualTo(1);
    }

    private List<User> searchMember2(String nameCond, Integer ageCond) {
        /**
         * where 절에 조건을 나열하면 and 조건으로 실행되는데, 이때 위에서 언급했던 것처럼
         * null이 들어가면 그 값은 무시한다. 따라서 param이 null이면 null 값을 리턴함으로써
         * 동적 필터링이 가능해진다.
         */
        return query
                .selectFrom(user)
                .where(nameEq(nameCond), ageEq(ageCond))
             // .where(nameAndAgeEq(nameCond, ageCond))
                .fetch();
    }

    /**
     * 삼항연산자 대신 early return이 가독성이 더 좋은 것 같다.
     */
    private BooleanExpression nameEq(String nameCond) {
        if (nameCond == null) {
            return null;
        }

        return user.name.eq(nameCond);
    }

    private BooleanExpression ageEq(Integer ageCond) {
        if (ageCond == null) {
            return null;
        }

        return user.age.eq(ageCond);
    }
```
- 위와 같이(nameEq, ageEq 메소드) 코드가 복잡하지 않고 if문이 추가로 늘어나는 상황이 아닌 경우에는 early return 패턴이 삼항연산자보다 좀 더 가독성이 좋은 것 같다.
 
 
- 이렇게 조건을 메소드로 분리해서 활용하게 되면 `BooleanBuilder`를 사용했을 때와는 다르게 쿼리의 가독성을 훨씬 더 높일 수 있다. 아래 예시를 살펴보자.

```java
    private List<User> query1(...) {
        return query
                .selectFrom(...)
                .where(isServiceable())
                .fetch();
    }
    
    private List<User> query2(...) {
        return query
                .selectFrom(...)
                .where(isServiceable())
                .fetch();
    }

    ...

    private BooleanExpression isServiceable(...) {
        return isValid().and(dateBetween());
    }
```

- 위와 같이 여러 메소드를 조합해서 where 절에 들어갈 조건을 조합하게 되면 네이밍이 가능해지므로 보다 쿼리 자체의 역할을 이해하기 쉬워진다.
- 또한 재사용이 용이해진다.