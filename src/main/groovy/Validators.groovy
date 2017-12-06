class StringValidator {
    final int size

    StringValidator(size = -1) {
        this.size = size
    }

    def validate(String data) {
        size == -1 ? true : data.length() <= size
    }
}

class IntegerValidator {
    final int length

    IntegerValidator(length = 10) {
        this.length = length
    }

    def validate(int data) {
        data.toString().length() <= length
//        data.length() <= length
    }
}

class SetValidator {
    final List<String> validValues

    SetValidator(validValues) {
        this.validValues = validValues
    }

    def validate(String value) {
        validValues.contains(value)
    }
}

class EmptyValidator {
    EmptyValidator(constraints) {
    }

    def validate(String value) {
        false
    }
}