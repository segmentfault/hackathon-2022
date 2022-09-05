/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      fontSize: {
        '6xl': '4rem',
      },
      lineHeight: {
        12: '3rem',
      },
    },
  },
  plugins: [],
}
