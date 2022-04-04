package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static Map<Long, Member> store = new HashMap<>();

    private static long sequence = 0L;

    public Member save(Member member){
        member.setId(++sequence);
        log.info("save: member={}", member);

        ValueOperations<String, String> memberOperations = redisTemplate.opsForValue();
        memberOperations.set("memberKey", member.getName());

        store.put(member.getId(), member);

        //log.info("redis memberKey {}", memberOperations.get("memberKey"));
        return member;
    }

    public Member findById(Long id){
        return store.get(id);
    }

    public Optional<Member> findByLoginId(String loginId){

/*        List<Member> all = findAll();
        for (Member member : all) {
            if(member.getLoginId().equals(loginId)){
                return Optional.of(member);
            }
        }
        return Optional.empty();
  */
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();


    }

    public List<Member> findAll(){
        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }
}
