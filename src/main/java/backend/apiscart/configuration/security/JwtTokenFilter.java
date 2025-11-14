package backend.apiscart.configuration.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	private static final String HEADER = "Authorization";
	private static final String PREFIX = "Bearer ";

	@Value("${jwt.secret}")
	private String secretKey;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain chain)
			throws ServletException, IOException {

		try {
			if (hasJWT(request)) {
				Claims claims = validateToken(request);

				if (claims != null && claims.get("authorities") != null) {
					setUpSpringAuthentication(claims);
				} else {
					SecurityContextHolder.clearContext();
				}
			} else {
				SecurityContextHolder.clearContext();
			}

			chain.doFilter(request, response);

		} catch (ExpiredJwtException | UnsupportedJwtException |
				 MalformedJwtException | IllegalArgumentException e) {

			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
		}
	}

	private Claims validateToken(HttpServletRequest request) {
		String jwtToken = request.getHeader(HEADER);

		if (jwtToken == null || !jwtToken.startsWith(PREFIX)) {
			return null;
		}

		jwtToken = jwtToken.replace(PREFIX, "");

		try {
			Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

			return Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(jwtToken)
					.getBody();

		} catch (JwtException e) {
			System.out.println("Error al validar token JWT: " + e.getMessage());
			return null;
		}
	}

	private void setUpSpringAuthentication(Claims claims) {

		@SuppressWarnings("unchecked")
		List<String> authorities = (List<String>) claims.get("authorities");

		UsernamePasswordAuthenticationToken auth =
				new UsernamePasswordAuthenticationToken(
						claims.getSubject(),
						null,
						authorities.stream()
								.map(SimpleGrantedAuthority::new)
								.collect(Collectors.toList())
				);

		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	private boolean hasJWT(HttpServletRequest request) {
		String header = request.getHeader(HEADER);
		return header != null && header.startsWith(PREFIX);
	}
}
