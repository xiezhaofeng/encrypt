package com.encrypt.config;

//@Configuration
//@RestController
//@EnableOAuth2Client
//@EnableAuthorizationServer
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig{// extends WebSecurityConfigurerAdapter {
//
//    @Resource
//    private LopUserDetailService userDetailService;
//
//    @Resource
//    private OAuth2ClientContext oauth2ClientContext;
//
//
//    @RequestMapping("/user")
//    public String user(Principal user) {
//
//        return "Hello" + user.getName();
//    }
//
//    @Override
//    @Bean // share AuthenticationManager for web and oauth
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    /**
//     * 主过滤器
//     *
//     * @param http
//     * @throws Exception
//     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
////                .headers()
////                .frameOptions()
////                .sameOrigin()
////                .and()
//                //.csrf().disable()
//                // 跨域支持
//                .cors().and()
//                .antMatcher("/**")
//                .authorizeRequests()
//                .antMatchers("/", "/github", "/login**", "/webjars/**").permitAll()
//                .anyRequest().authenticated();
//        http
//                .exceptionHandling()
//                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
//                .and()
//                .formLogin().loginPage("/login").loginProcessingUrl("/login.do").defaultSuccessUrl("/success")
//                .failureUrl("/login?err=1")
//                .permitAll();
//        http
//                .logout()
//                .logoutUrl("/logout")               //默认只接受post请求处理,需要携带csrf token
//                .logoutSuccessUrl("/").permitAll()
//                .invalidateHttpSession(true)
//                .clearAuthentication(true)
//                .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());    //csrf for angular
//        http
//                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
//
//    }
//
//
//    /**
//     * 授权服务器(定义UserDetails类)
//     *
//     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth)
//            throws Exception {
//        // Configure spring security's authenticationManager with custom
//        // user details service
//        auth.userDetailsService(this.userDetailService);
//    }
//
//
//    /**
//     * 过滤器(第三方,需要注入到主过滤器)
//     *
//     */
//    private Filter ssoFilter() {
//        CompositeFilter filter = new CompositeFilter();
//        List<Filter> filters = new ArrayList<>();
//        filters.add(ssoFilter(github(), "/login/github"));
//        filter.setFilters(filters);
//        return filter;
//    }
//
//    @Bean
//    @ConfigurationProperties("github")
//    public ClientResources github() {
//        return new ClientResources();
//    }
//
//    /**
//     * 支持从本地重定向到第三方,由异常触发
//     *
//     */
//    @Bean
//    public FilterRegistrationBean oauth2ClientFilterRegistration(
//            OAuth2ClientContextFilter filter) {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(filter);
//        registration.setOrder(-100);
//        return registration;
//    }
//
//    /**
//     * 本地的资源服务器
//     *
//     */
//    @Configuration
//    @EnableResourceServer
//    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
//        @Override
//        public void configure(HttpSecurity http) throws Exception {
//            http.antMatcher("/api/**").authorizeRequests().anyRequest().authenticated();
//        }
//    }
//
//    private Filter ssoFilter(ClientResources client, String path) {
//        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(path);
//        OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
//        filter.setRestTemplate(template);
//        UserInfoTokenServices tokenServices = new UserInfoTokenServices(
//                client.getResource().getUserInfoUri(), client.getClient().getClientId());
//        tokenServices.setRestTemplate(template);
//        filter.setTokenServices(tokenServices);
//        return filter;
//    }
//
//    class ClientResources {
//
//        @NestedConfigurationProperty
//        private AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();
//
//        @NestedConfigurationProperty
//        private ResourceServerProperties resource = new ResourceServerProperties();
//
//        public AuthorizationCodeResourceDetails getClient() {
//            return client;
//        }
//
//        public ResourceServerProperties getResource() {
//            return resource;
//        }
//    }
}