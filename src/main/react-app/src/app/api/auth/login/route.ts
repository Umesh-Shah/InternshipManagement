import { NextResponse } from 'next/server'

export async function POST(request: Request) {
    try {
        const body = await request.json()
        const { username, password } = body

        // TODO: Replace with actual authentication logic
        // This is just a placeholder that simulates a backend call
        const response = await fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password }),
        })

        if (!response.ok) {
            return NextResponse.json(
                { message: 'Invalid credentials' },
                { status: 401 }
            )
        }

        const data = await response.json()

        // Set the JWT token in an HTTP-only cookie
        const headers = new Headers()
        headers.append(
            'Set-Cookie',
            `token=${data.token}; HttpOnly; Path=/; SameSite=Strict`
        )

        return NextResponse.json(
            { message: 'Login successful' },
            { status: 200, headers }
        )
    } catch (error) {
        console.error('Login error:', error)
        return NextResponse.json(
            { message: 'Internal server error' },
            { status: 500 }
        )
    }
} 