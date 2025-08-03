class Squares {
	let n: Int

	// initialize the class with a number n
	init(_ n: Int) {
		self.n = n
	}

	// square of the sum (1 + 2 + ... + n)^2
	var squareOfSum: Int {
		let sum: Int = (1...n).reduce(0, +)
		return sum * sum
	}

	// sum of squares 1^2 + 2^2 + ... + n^2
	var sumOfSquares: Int {
		return (1...n).reduce(0) { result, number in
			result + number * number
		}
	}

	// difference between square of the sum and sum of squares
	var differenceOfSquares: Int {
		return squareOfSum - sumOfSquares
	}

}
