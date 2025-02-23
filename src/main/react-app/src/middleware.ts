import { NextResponse } from 'next/server'
import type { NextRequest } from 'next/server'

// Add any paths that should be accessible without authentication
const publicPaths = [
    '/',
    '/login',
    '/register',
    '/api/auth/login',
    '/api/auth/register',
]

export function middleware(request: NextRequest) {
    const token = request.cookies.get('token')
    const { pathname } = request.nextUrl

    // Allow access to public paths
    if (publicPaths.some((path) => pathname.startsWith(path))) {
        return NextResponse.next()
    }

    // Redirect to login if no token is present
    if (!token) {
        const loginUrl = new URL('/login', request.url)
        loginUrl.searchParams.set('from', pathname)
        return NextResponse.redirect(loginUrl)
    }

    // TODO: Add token validation logic here
    // For now, we'll just check if the token exists

    return NextResponse.next()
}

export const config = {
    matcher: [
        /*
         * Match all request paths except for the ones starting with:
         * - _next/static (static files)
         * - _next/image (image optimization files)
         * - favicon.ico (favicon file)
         * - public folder
         */
        '/((?!_next/static|_next/image|favicon.ico|public/).*)',
    ],
} 