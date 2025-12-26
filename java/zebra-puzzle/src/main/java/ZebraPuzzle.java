import java.util.ArrayList;
import java.util.List;

class ZebraPuzzle {

    // ========== JAVA ENUMS vs PYTHON STRINGS/ENUMS ==========
    // Python: colors = ["red", "green", "ivory", "yellow", "blue"]
    //    or:  from enum import Enum; class Color(Enum): RED = 1 ...
    // Java enums are type-safe - compiler catches typos like "rad" vs "red"
    enum Color { RED, GREEN, IVORY, YELLOW, BLUE }
    enum Nationality { ENGLISHMAN, SPANIARD, UKRAINIAN, NORWEGIAN, JAPANESE }
    enum Pet { DOG, SNAIL, FOX, HORSE, ZEBRA }
    enum Drink { COFFEE, TEA, MILK, ORANGE_JUICE, WATER }
    enum Hobby { DANCING, PAINTING, READING, FOOTBALL, CHESS }

    // ========== INNER CLASS vs PYTHON DATACLASS/DICT ==========
    // Python equivalent:
    //   @dataclass
    //   class House:
    //       position: int
    //       color: Color = None
    //       nationality: Nationality = None
    //       ...
    // Or simply: house = {"position": 1, "color": None, ...}
    private static class House {
        int position;  // 1 to 5
        Color color;
        Nationality nationality;
        Pet pet;
        Drink drink;
        Hobby hobby;

        // Java requires explicit constructor
        // Python: def __init__(self, position): self.position = position
        House(int position) {
            this.position = position;
        }

        // For creating a copy (needed for backtracking)
        // Python: import copy; new_house = copy.deepcopy(house)
        House copy() {
            House h = new House(this.position);
            h.color = this.color;
            h.nationality = this.nationality;
            h.pet = this.pet;
            h.drink = this.drink;
            h.hobby = this.hobby;
            return h;
        }
    }

    private String waterDrinker;
    private String zebraOwner;

    ZebraPuzzle() {
        solve();
    }

    // ========== MAIN SOLVING LOGIC ==========
    // Strategy: Generate all permutations of each attribute and check constraints
    // This is brute-force but with early pruning via constraint checks
    //
    // Python equivalent using itertools:
    //   from itertools import permutations
    //   for colors in permutations(Color):
    //       for nationalities in permutations(Nationality):
    //           ...
    //           if check_constraints(houses):
    //               return houses
    private void solve() {
        // Create 5 houses at positions 1-5
        // Python: houses = [House(i) for i in range(1, 6)]
        List<House> houses = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            houses.add(new House(i));
        }

        // Generate all permutations and find valid solution
        // We nest 5 loops of permutations (one per attribute)
        // Java doesn't have itertools.permutations, so we use recursion
        
        // Get all permutation arrays
        // Python: list(permutations([0,1,2,3,4])) -> [[0,1,2,3,4], [0,1,2,4,3], ...]
        List<int[]> perms = generatePermutations(5);

        // Color values array for indexing
        // Python: colors = list(Color)
        Color[] colors = Color.values();
        Nationality[] nationalities = Nationality.values();
        Pet[] pets = Pet.values();
        Drink[] drinks = Drink.values();
        Hobby[] hobbies = Hobby.values();

        // Try all combinations of permutations
        // This is O(5!^5) = 24 billion in worst case, but constraints prune heavily
        // Python: for color_perm in permutations(range(5)):
        for (int[] colorPerm : perms) {
            // Assign colors to houses based on permutation
            // Python: for i, house in enumerate(houses): house.color = colors[color_perm[i]]
            for (int i = 0; i < 5; i++) {
                houses.get(i).color = colors[colorPerm[i]];
            }

            // Early constraint check: green is right of ivory (clue 6)
            // Python: if not green_right_of_ivory(houses): continue
            if (!checkColorConstraints(houses)) continue;

            for (int[] natPerm : perms) {
                for (int i = 0; i < 5; i++) {
                    houses.get(i).nationality = nationalities[natPerm[i]];
                }

                // Early check: Norwegian in house 1, next to blue (clues 10, 15)
                // Englishman in red house (clue 2)
                if (!checkNationalityConstraints(houses)) continue;

                for (int[] drinkPerm : perms) {
                    for (int i = 0; i < 5; i++) {
                        houses.get(i).drink = drinks[drinkPerm[i]];
                    }

                    // Early check: milk in middle, coffee in green, Ukrainian drinks tea (clues 4, 5, 9)
                    if (!checkDrinkConstraints(houses)) continue;

                    for (int[] hobbyPerm : perms) {
                        for (int i = 0; i < 5; i++) {
                            houses.get(i).hobby = hobbies[hobbyPerm[i]];
                        }

                        // Check hobby constraints (clues 7, 8, 13, 14)
                        if (!checkHobbyConstraints(houses)) continue;

                        for (int[] petPerm : perms) {
                            for (int i = 0; i < 5; i++) {
                                houses.get(i).pet = pets[petPerm[i]];
                            }

                            // Final check: all pet constraints (clues 3, 11, 12)
                            if (checkPetConstraints(houses)) {
                                // Found valid solution!
                                extractSolution(houses);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    // ========== PERMUTATION GENERATOR ==========
    // Python: list(itertools.permutations(range(n)))
    // Java doesn't have this built-in, so we implement it
    private List<int[]> generatePermutations(int n) {
        List<int[]> result = new ArrayList<>();
        int[] arr = new int[n];
        // Python: arr = list(range(n))
        for (int i = 0; i < n; i++) arr[i] = i;
        permute(arr, 0, result);
        return result;
    }

    // Recursive permutation using swapping (Heap's algorithm variant)
    // Python equivalent:
    //   def permute(arr, start, result):
    //       if start == len(arr):
    //           result.append(arr.copy())
    //       for i in range(start, len(arr)):
    //           arr[start], arr[i] = arr[i], arr[start]
    //           permute(arr, start + 1, result)
    //           arr[start], arr[i] = arr[i], arr[start]
    private void permute(int[] arr, int start, List<int[]> result) {
        if (start == arr.length) {
            // Python: result.append(arr.copy())
            // Java: must clone array since arrays are mutable references
            result.add(arr.clone());
            return;
        }
        for (int i = start; i < arr.length; i++) {
            // Python: arr[start], arr[i] = arr[i], arr[start]
            swap(arr, start, i);
            permute(arr, start + 1, result);
            swap(arr, start, i);  // backtrack
        }
    }

    // Java doesn't have tuple unpacking for swaps
    // Python: a, b = b, a
    // Java: need a helper method
    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // ========== CONSTRAINT CHECKS ==========
    // Split into groups for early pruning (fail fast)

    // Clue 6: Green house is immediately to the right of ivory house
    private boolean checkColorConstraints(List<House> houses) {
        House green = findByColor(houses, Color.GREEN);
        House ivory = findByColor(houses, Color.IVORY);
        return isRightOf(green, ivory);
    }

    // Clues 2, 10, 15: Englishman-red, Norwegian-first, Norwegian-next-to-blue
    private boolean checkNationalityConstraints(List<House> houses) {
        // Clue 10: Norwegian in first house
        // Python: if houses[0].nationality != Nationality.NORWEGIAN: return False
        if (houses.get(0).nationality != Nationality.NORWEGIAN) return false;

        // Clue 2: Englishman lives in red house
        House englishman = findByNationality(houses, Nationality.ENGLISHMAN);
        if (englishman.color != Color.RED) return false;

        // Clue 15: Norwegian lives next to blue house
        House norwegian = findByNationality(houses, Nationality.NORWEGIAN);
        House blue = findByColor(houses, Color.BLUE);
        if (!areNeighbors(norwegian, blue)) return false;

        return true;
    }

    // Clues 4, 5, 9: Coffee-green, Ukrainian-tea, milk-middle
    private boolean checkDrinkConstraints(List<House> houses) {
        // Clue 9: Person in middle house (position 3) drinks milk
        // Python: if houses[2].drink != Drink.MILK: return False
        if (houses.get(2).drink != Drink.MILK) return false;

        // Clue 4: Person in green house drinks coffee
        House green = findByColor(houses, Color.GREEN);
        if (green.drink != Drink.COFFEE) return false;

        // Clue 5: Ukrainian drinks tea
        House ukrainian = findByNationality(houses, Nationality.UKRAINIAN);
        if (ukrainian.drink != Drink.TEA) return false;

        return true;
    }

    // Clues 7, 8, 13, 14: Dancing-snail, yellow-painter, football-OJ, Japanese-chess
    private boolean checkHobbyConstraints(List<House> houses) {
        // Clue 8: Person in yellow house is a painter
        House yellow = findByColor(houses, Color.YELLOW);
        if (yellow.hobby != Hobby.PAINTING) return false;

        // Clue 13: Person who plays football drinks orange juice
        House footballer = findByHobby(houses, Hobby.FOOTBALL);
        if (footballer.drink != Drink.ORANGE_JUICE) return false;

        // Clue 14: Japanese person plays chess
        House japanese = findByNationality(houses, Nationality.JAPANESE);
        if (japanese.hobby != Hobby.CHESS) return false;

        return true;
    }

    // Clues 3, 7, 11, 12: Spaniard-dog, snail-dancing, reader-next-to-fox, painter-next-to-horse
    private boolean checkPetConstraints(List<House> houses) {
        // Clue 3: Spaniard owns the dog
        House spaniard = findByNationality(houses, Nationality.SPANIARD);
        if (spaniard.pet != Pet.DOG) return false;

        // Clue 7: Snail owner likes dancing
        House snailOwner = findByPet(houses, Pet.SNAIL);
        if (snailOwner.hobby != Hobby.DANCING) return false;

        // Clue 11: Reader lives next to fox owner
        House reader = findByHobby(houses, Hobby.READING);
        House foxOwner = findByPet(houses, Pet.FOX);
        if (!areNeighbors(reader, foxOwner)) return false;

        // Clue 12: Painter's house is next to horse owner
        House painter = findByHobby(houses, Hobby.PAINTING);
        House horseOwner = findByPet(houses, Pet.HORSE);
        if (!areNeighbors(painter, horseOwner)) return false;

        return true;
    }

    // Extract answers from solved houses
    private void extractSolution(List<House> houses) {
        // Python: water_drinker = next(h for h in houses if h.drink == Drink.WATER)
        for (House h : houses) {
            if (h.drink == Drink.WATER) {
                waterDrinker = formatNationality(h.nationality);
            }
            if (h.pet == Pet.ZEBRA) {
                zebraOwner = formatNationality(h.nationality);
            }
        }
    }

    // ========== HELPER METHODS ==========
    // These are like Python list comprehensions with next()

    // Python: next((h for h in houses if h.nationality == nat), None)
    private House findByNationality(List<House> houses, Nationality nat) {
        for (House h : houses) {
            if (h.nationality == nat) return h;
        }
        return null;
    }

    // Python: next((h for h in houses if h.color == color), None)
    private House findByColor(List<House> houses, Color color) {
        for (House h : houses) {
            if (h.color == color) return h;
        }
        return null;
    }

    // Python: next((h for h in houses if h.pet == pet), None)
    private House findByPet(List<House> houses, Pet pet) {
        for (House h : houses) {
            if (h.pet == pet) return h;
        }
        return null;
    }

    // Python: next((h for h in houses if h.hobby == hobby), None)
    private House findByHobby(List<House> houses, Hobby hobby) {
        for (House h : houses) {
            if (h.hobby == hobby) return h;
        }
        return null;
    }

    // Python: abs(h1.position - h2.position) == 1
    private boolean areNeighbors(House h1, House h2) {
        if (h1 == null || h2 == null) return false;
        return Math.abs(h1.position - h2.position) == 1;
    }

    // Python: h1.position == h2.position + 1
    private boolean isRightOf(House h1, House h2) {
        if (h1 == null || h2 == null) return false;
        return h1.position == h2.position + 1;
    }

    // Python: nat.name.capitalize()  (e.g., "ENGLISHMAN" -> "Englishman")
    private String formatNationality(Nationality nat) {
        String name = nat.name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }

    String getWaterDrinker() {
        return waterDrinker;
    }

    String getZebraOwner() {
        return zebraOwner;
    }
}
