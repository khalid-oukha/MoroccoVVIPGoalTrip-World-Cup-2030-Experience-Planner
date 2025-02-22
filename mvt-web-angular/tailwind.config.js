// const colors = require('tailwindcss/colors');

module.exports = {
  prefix: '',
  mode: 'jit',
  important: false,
  content: ['./src/**/*.{html,ts}'],
  darkMode: 'class',
  theme: {
    extend: {
      keyframes: {
        wiggle: {
          '0%, 100%': { transform: 'rotate(-3deg)' },
          '50%': { transform: 'rotate(3deg)' },
        },
        'fade-in-down': {
          '0%': {
            opacity: '0',
            transform: 'translateY(-10px)',
          },
          '100%': {
            opacity: '1',
            transform: 'translateY(0)',
          },
        },
        'fade-out-down': {
          from: {
            opacity: '1',
            transform: 'translateY(0px)',
          },
          to: {
            opacity: '0',
            transform: 'translateY(10px)',
          },
        },
        'fade-in-up': {
          '0%': {
            opacity: '0',
            transform: 'translateY(10px)',
          },
          '100%': {
            opacity: '1',
            transform: 'translateY(0)',
          },
        },
        'fade-out-up': {
          from: {
            opacity: '1',
            transform: 'translateY(0px)',
          },
          to: {
            opacity: '0',
            transform: 'translateY(10px)',
          },
        },
      },
      animation: {
        wiggle: 'wiggle 1s ease-in-out infinite',
        'fade-in-down': 'fade-in-down 0.3s ease-out',
        'fade-out-down': 'fade-out-down 0.3s ease-out',
        'fade-in-up': 'fade-in-up 0.3s ease-out',
        'fade-out-up': 'fade-out-up 0.3s ease-out',
      },
      boxShadow: {
        custom: '0px 0px 50px 0px rgb(82 63 105 / 15%)',
      },
      colors: {
        border: 'hsl(var(--border))',
        background: 'hsl(var(--background))',
        foreground: 'hsl(var(--foreground))',
        primary: {
          DEFAULT: 'hsl(var(--primary))',
          foreground: 'hsl(var(--primary-foreground))',
        },
        destructive: {
          DEFAULT: 'hsl(var(--destructive))',
          foreground: 'hsl(var(--destructive-foreground))',
        },
        muted: {
          DEFAULT: 'hsl(var(--muted))',
          foreground: 'hsl(var(--muted-foreground))',
        },
        card: {
          DEFAULT: 'hsl(var(--card))',
          foreground: 'hsl(var(--card-foreground))',
        },      dune: {
          50: '#F9F8F6',
          100: '#F2F0EC',
          200: '#E5E1D9',
          300: '#D8D2C6',
          400: '#CBC3B3',
          500: '#BEB4A0',
          600: '#968D7E',
          700: '#6E675C',
          800: '#46423A',
          900: '#1E1C18',
        },
        oasis: {
          50: '#F0F5F7',
          100: '#E1EBEF',
          200: '#C3D7DF',
          300: '#A5C3CF',
          400: '#87AFBF',
          500: '#699BAF',
          600: '#547C8C',
          700: '#3F5D69',
          800: '#2A3E46',
          900: '#151F23',
        },
        terracotta: {
          50: '#FAF4F2',
          100: '#F5E9E5',
          200: '#EBD3CB',
          300: '#E1BDB1',
          400: '#D7A797',
          500: '#CD917D',
          600: '#A47464',
          700: '#7B574B',
          800: '#523A32',
          900: '#291D19',
        },
        clay: {
          50: '#F7F5F3',
          100: '#EFEBE7',
          200: '#DFD7CF',
          300: '#CFC3B7',
          400: '#BFAF9F',
          500: '#AF9B87',
          600: '#8C7C6C',
          700: '#695D51',
          800: '#463E36',
          900: '#231F1B',
        },
      },
    },
    fontFamily: {
      poppins: ['Poppins', 'system-ui', 'sans-serif'],
      nunito: ['Nunito Sans', 'sans-serif'],
    },
    container: {
      padding: {
        DEFAULT: '1rem',
        sm: '2rem',
        lg: '4rem',
        xl: '5rem',
        '2xl': '6rem',
      },
    },
  },
  variants: {
    extend: {},
    scrollbar: ['dark', 'rounded'],
  },
  plugins: [
    require('@tailwindcss/forms'),
    require('@tailwindcss/typography'),
    require('@tailwindcss/aspect-ratio'),
    require('tailwind-scrollbar'),
    'prettier-plugin-tailwindcss',
  ],
};
