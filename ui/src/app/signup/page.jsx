export default function page() {
  return (
    <div className="flex flex-col justify-center items-center min-h-screen bg-gray-100 dark:bg-gray-900">
      <h1 className="flex justify-center">
        <img src="/assets/images/logo.png" alt="logo" className="w-32 invert dark:invert-0" />
      </h1>
      <div className="w-[400px] bg-white dark:bg-gray-800 p-6 rounded-lg shadow-lg">
        {/* Tiêu đề */}
        <h2 className="text-center text-xl font-semibold dark:text-white">
          Create a new account
        </h2>
        <p className="text-center text-gray-600 dark:text-gray-400 text-sm mb-4">
          It's quick and easy.
        </p>

        {/* Form */}
        <div className="space-y-3">
          {/* Tên */}
          <div className="flex space-x-2">
            <input
              type="text"
              placeholder="Family name"
              className="w-1/2 p-2 border border-gray-300 dark:border-gray-700 rounded-md focus:ring-2 focus:ring-blue-500 dark:bg-gray-700 dark:text-white"
            />
            <input
              type="text"
              placeholder="Given name"
              className="w-1/2 p-2 border border-gray-300 dark:border-gray-700 rounded-md focus:ring-2 focus:ring-blue-500 dark:bg-gray-700 dark:text-white"
            />
          </div>

          {/* Ngày sinh */}
          <input
            type="date"
            placeholder="Date of birth"
            className="w-full p-2 border border-gray-300 dark:border-gray-700 rounded-md focus:ring-2 focus:ring-blue-500 dark:bg-gray-700 dark:text-white"
          />

          {/* Giới tính */}
          <div className="flex space-x-2">
            <label className="flex-1 flex items-center justify-between p-2 border border-gray-300 dark:border-gray-700 rounded-md dark:bg-gray-700 dark:text-white">
              Female <input type="radio" name="gender" className="ml-2" />
            </label>
            <label className="flex-1 flex items-center justify-between p-2 border border-gray-300 dark:border-gray-700 rounded-md dark:bg-gray-700 dark:text-white">
              Male <input type="radio" name="gender" className="ml-2" />
            </label>
            <label className="flex-1 flex items-center justify-between p-2 border border-gray-300 dark:border-gray-700 rounded-md dark:bg-gray-700 dark:text-white">
              Custom <input type="radio" name="gender" className="ml-2" />
            </label>
          </div>

          {/* Email và mật khẩu */}
          <input
            type="text"
            placeholder="Email address"
            className="w-full p-2 border border-gray-300 dark:border-gray-700 rounded-md focus:ring-2 focus:ring-blue-500 dark:bg-gray-700 dark:text-white"
          />
          <input
            type="password"
            placeholder="Password"
            className="w-full p-2 border border-gray-300 dark:border-gray-700 rounded-md focus:ring-2 focus:ring-blue-500 dark:bg-gray-700 dark:text-white"
          />

          {/* Điều khoản */}
          <p className="text-xs text-gray-600 dark:text-gray-400">
            People who use our service may have uploaded your contact
            information to Z.{" "}
            <a href="#" className="text-blue-600 dark:text-blue-400">
              Learn more.
            </a>
          </p>
          <p className="text-xs text-gray-600 dark:text-gray-400">
            By clicking Sign Up, you agree to our{" "}
            <a href="#" className="text-blue-600 dark:text-blue-400">
              Terms
            </a>
            ,{" "}
            <a href="#" className="text-blue-600 dark:text-blue-400">
              Privacy Policy
            </a>
            , and{" "}
            <a href="#" className="text-blue-600 dark:text-blue-400">
              Cookies Policy
            </a>
            .
          </p>

          {/* Nút đăng ký */}
          <button className="w-full bg-green-500 text-white p-2 rounded-md font-bold hover:bg-green-600 dark:bg-green-600 dark:hover:bg-green-700">
            Sign Up
          </button>

          {/* Liên kết đăng nhập */}
          <p className="text-center text-blue-600 dark:text-blue-400 mt-2 cursor-pointer hover:underline">
            Already have an account?
          </p>
        </div>
      </div>
    </div>
  );
}
