package com.rexoit.mywallpaper.util

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
	companion object {
		fun <T> success(data: T): Resource<T> =
			Resource(status = Status.SUCCESS, data = data, message = null)

		fun <T> fail(message: String?): Resource<T> =
			Resource(status = Status.FAIL, data = null, message = message)
	}
}
