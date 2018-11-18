<!--
  -    Copyright (c) 2018-2025, lengleng All rights reserved.
  -
  - Redistribution and use in source and binary forms, with or without
  - modification, are permitted provided that the following conditions are met:
  -
  - Redistributions of source code must retain the above copyright notice,
  - this list of conditions and the following disclaimer.
  - Redistributions in binary form must reproduce the above copyright
  - notice, this list of conditions and the following disclaimer in the
  - documentation and/or other materials provided with the distribution.
  - Neither the name of the pig4cloud.com developer nor the names of its
  - contributors may be used to endorse or promote products derived from
  - this software without specific prior written permission.
  - Author: lengleng (wangiegie@gmail.com)
  -->

<template>
  <div class="section">
    <div class="container">
      <iframe src="/hystrix" class="hystrix"></iframe>
    </div>
  </div>
</template>

<script>
  export default {
    data: () => ({
      // eslint-disable-next-line no-undef
      version: __PROJECT_VERSION__
    }),
    install({viewRegistry}) {
      viewRegistry.addView({
        path: '/turbine',
        name: 'turbine',
        label: 'Turbine',
        order: 200,
        component: this
      });
    }
  };
</script>
<style>
  .hystrix{
    width: 100%;
    height: 600px;
  }
</style>
