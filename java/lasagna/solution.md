# Lasagna Exercise: Java vs Python Solution

## Problem Summary
Calculate cooking times for lasagna:
1. Expected oven time: 40 minutes
2. Remaining oven time: expected - actual
3. Preparation time: 2 minutes per layer
4. Total time: preparation + oven time

---

## Java Solution

```java
public class Lasagna {
    public int expectedMinutesInOven() {
        return 40;
    }
    
    public int remainingMinutesInOven(int actualMinutes) {
        return expectedMinutesInOven() - actualMinutes;
    }
    
    public int preparationTimeInMinutes(int layers) {
        return layers * 2;
    }
    
    public int totalTimeInMinutes(int layers, int actualMinutes) {
        return preparationTimeInMinutes(layers) + actualMinutes;
    }
}
```

**Usage:**
```java
Lasagna lasagna = new Lasagna();
lasagna.expectedMinutesInOven();           // => 40
lasagna.remainingMinutesInOven(30);        // => 10
lasagna.preparationTimeInMinutes(2);       // => 4
lasagna.totalTimeInMinutes(3, 20);         // => 26
```

---

## Python Equivalent

```python
class Lasagna:
    def expected_minutes_in_oven(self):
        return 40
    
    def remaining_minutes_in_oven(self, actual_minutes):
        return self.expected_minutes_in_oven() - actual_minutes
    
    def preparation_time_in_minutes(self, layers):
        return layers * 2
    
    def total_time_in_minutes(self, layers, actual_minutes):
        return self.preparation_time_in_minutes(layers) + actual_minutes
```

**Usage:**
```python
lasagna = Lasagna()
lasagna.expected_minutes_in_oven()         # => 40
lasagna.remaining_minutes_in_oven(30)      # => 10
lasagna.preparation_time_in_minutes(2)     # => 4
lasagna.total_time_in_minutes(3, 20)       # => 26
```

---

## Key Syntax Differences

| Aspect | Java | Python |
|--------|------|--------|
| **Type Declaration** | `public int methodName()` | `def method_name(self):` |
| **Return Type** | Must declare (`int`, `String`, etc.) | Inferred dynamically |
| **Parameter Types** | Must declare (`int actualMinutes`) | No type declaration needed |
| **Access Modifiers** | `public`, `private`, `protected` | Not required (convention: `_private`) |
| **Naming Convention** | camelCase | snake_case |
| **Self Reference** | Implicit `this` (not in params) | Explicit `self` (first param) |
| **Statement End** | Semicolon `;` required | No semicolon |
| **Instantiation** | `new Lasagna()` | `Lasagna()` |
| **Code Blocks** | Curly braces `{}` | Indentation |

---

## Logic Breakdown

### 1. Expected Minutes
Simply returns constant value 40.

### 2. Remaining Minutes
```
remaining = expected - actual
remaining = 40 - 30 = 10
```

### 3. Preparation Time
```
prep_time = layers × 2 minutes
prep_time = 2 × 2 = 4 minutes
```

### 4. Total Time
```
total = prep_time + oven_time
total = (3 × 2) + 20 = 6 + 20 = 26 minutes
```

---

## Java-Specific Notes for Python Developers

1. **Static Typing**: Every variable and method return must have explicit type
2. **Compilation**: Java compiles to bytecode before running (Python interprets)
3. **Method Overloading**: Java supports multiple methods with same name but different parameters
4. **No Default Arguments**: Java doesn't support default parameter values like Python
5. **Primitive Types**: `int`, `double`, `boolean` are primitives (not objects) in Java
6. **String Immutability**: Similar to Python, but Java has `StringBuilder` for mutable strings
