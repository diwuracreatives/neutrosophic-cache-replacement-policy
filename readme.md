# Neutrosophic Cache

A Java cache replacement policy based on 2-refined neutrosophic integral-based utility functions.

Each cache item is modeled as:

$$U(t) = T(t) + I_1(t) \cdot i_1 + I_2(t) \cdot i_2 + F(t)$$

Eviction decisions are determined by convergence of the improper neutrosophic integral of the first kind where convergence flags eviction while divergence retention.

---

## Installation

### Maven

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.diwuracreatives</groupId>
    <artifactId>neutrosophic-cache-replacement-policy</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

**Groovy (`build.gradle`):**
```groovy
implementation 'io.github.diwuracreatives:neutrosophic-cache-replacement-policy:1.0.0'
```

**Kotlin DSL (`build.gradle.kts`):**
```kotlin
implementation("io.github.diwuracreatives:neutrosophic-cache-replacement-policy:1.0.0")
```

---

## Usage

```
import com.neutrosophicache.NeutrosophicCache;

NeutrosophicCache<K, V> cache = new NeutrosophicCache<>(capacity);

cache.put(key, value);
V item = cache.get(key);
```


## Learn More

- **Website Documentation:** [neutrosophicache.boluwatifeadediwura.xyz](https://neutrosophicache.boluwatifeadediwura.xyz/)
- **Research Paper:** [A Neutrosophic Cache Replacement Policy Using Improper Integral-Based Utility Functions](https://www.researchgate.net/publication/412950618_A_Neutrosophic_Cache_Replacement_Policy_Using_Improper_Integral-Based_Utility_Functions)

---

## License

MIT License. See [LICENSE](LICENSE) for details.