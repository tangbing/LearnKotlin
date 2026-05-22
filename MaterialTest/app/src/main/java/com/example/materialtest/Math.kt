package com.example.materialtest

   fun <T: Comparable<T>> max(vararg nums: T): T {
        if (nums.isEmpty()) throw RuntimeException("params can not be empty.")
        var maxNum = nums[0]
        for (num in nums) {
            if (num > maxNum) {
                maxNum = num
            }
        }
        return maxNum
    }



