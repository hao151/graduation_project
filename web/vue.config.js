const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,
  chainWebpack: (config) => {
    config
     .plugin('define')
     .tap((args) => {
        args[0] = {
          ...args[0],
          __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: false,
          __VUE_OPTIONS_API__: true,
          __VUE_PROD_DEVTOOLS__: false
        };
        return args;
      });
  }
});